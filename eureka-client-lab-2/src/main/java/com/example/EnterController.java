package com.example;


import com.example.Messages.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableDiscoveryClient
@Controller
public class EnterController {

    @Autowired
    private MsgProducer producer;
    private static Logger log = LoggerFactory.getLogger(EurekaClientLab2Application.class);
    private final RestTemplate restTemplate;

    @Autowired
    public EnterController(RestTemplateBuilder restTemplateBuilder,
                           RestTemplateResponseErrorHandler myResponseErrorHandler
    ) {

        this.restTemplate = restTemplateBuilder
                .errorHandler(myResponseErrorHandler)
                .build();
    }

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private Environment env;

    //instances

    @RequestMapping(value = "/lab-2_instances")
    public String getInstancesRun(){
        ServiceInstance instance = client.choose("lab-2");
        return instance.getUri().toString();
    }

    @RequestMapping(value = "/label_instances")
    public String getLabelInstancesRun(){
        ServiceInstance instance = client.choose("label");
        return instance.getUri().toString();
    }

    /* REFRESH */
    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
    public String checkRefresh() throws JsonProcessingException {
        return refresh() + getPropertiesClient();
    }
    @PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
    public String refresh()
    {
        return "Refreshed";
    }
    @GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
    public String getPropertiesClient() throws JsonProcessingException{
        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
        for (String propertyName : bootstrapProperties.getPropertyNames()) {
            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(props);
    }

    @Autowired
    UserService userRep;

    @Autowired
    RoleService roleRep;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username or password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        return "login";
    }
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        return "registration";
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration_post(@ModelAttribute User user, String role) {
        user.setEnabled(true);
        userRep.save(user);
        Authorities a = new Authorities(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        return "redirect:/login";
    }



    ///////

    @GetMapping("/songs")
    public ModelAndView getSongsView(Model model) {
        ModelAndView view = new ModelAndView("songAll");
        String url = getInstancesRun();
        log.info("Getting all songs from " + url);
        List<Song> songs = restTemplate.getForObject(String.format("%s/songs", url), List.class);
        List<Album> albums = restTemplate.getForObject(String.format("%s/albums", url), List.class);
        List<Singer> singers = restTemplate.getForObject(String.format("%s/singers", url), List.class);

        albums.add(0, null);
        singers.add(0, null);

        view.addObject("SongList", songs);
        view.addObject("Albums", albums);
        view.addObject("Singers", singers);
        return view;
    }
    @RequestMapping(value = "/songs/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getSong(@PathVariable Long id) {
        ModelAndView view = new ModelAndView("songDetail");
        String url = getInstancesRun();
        Song object = restTemplate.getForObject(String.format("%s/songs/%s", url, id), Song.class);
        List<Album> albums = restTemplate.getForObject(String.format("%s/albums", url), List.class);
        List<Singer> singers = restTemplate.getForObject(String.format("%s/singers", url), List.class);
        albums.add(0, null);
        singers.add(0, null);
        if(object.getId() == 0)
            view.addObject("error", "We have not song with id: #" + id );
        else
            view.addObject("Song", object);
        view.addObject("Albums", albums);
        view.addObject("Singers", singers);
        return view;
    }

    @RequestMapping(value = "/songs", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createSong(@RequestParam("title")String title,
                                   @RequestParam("content")String content,
                                   @RequestParam("album") String album,
                                   @RequestParam("singer") String singer) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("songs");
        }
        ModelAndView view = new ModelAndView("redirect:/songs");
        String url = getInstancesRun();
        log.info("Posting Song from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Song> entity = new HttpEntity<>(new Song(title, content), headers);

        try{
            ResponseEntity response = this.restTemplate.exchange(String.format("%s/songs", url),
                    HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                    });

            Song[] ar = restTemplate.getForObject(String.format("%s/songs", url), Song[].class);
            if(!album.isEmpty() && Integer.valueOf(album) > 0){
                ResponseEntity res = this.restTemplate.exchange(String.format("%s/songs/%s/album/%s", url, ar[ar.length-1].getId(), album),
                        HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                        });
            }
            if(!singer.isEmpty() && Integer.valueOf(singer) > 0){
                ResponseEntity res = this.restTemplate.exchange(String.format("%s/songs/%s/singer/%s", url, ar[ar.length-1].getId(), singer),
                        HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                        });
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return view;
    }
    @RequestMapping(value = "/songs/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateSong(@RequestParam("title")String title,
                                   @RequestParam("content")String content,
                                   @RequestParam("album") String album,
                                   @RequestParam("singer") String singer,
                                   @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("songs");
        }
        ModelAndView view = new ModelAndView("redirect:/songs/" + id);
        String url = getInstancesRun();
        log.info("Updating Song from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Song> entity = new HttpEntity<>(new Song(title, content), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/songs/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        if(!album.isEmpty() && Integer.valueOf(album) > 0){
            ResponseEntity res = this.restTemplate.exchange(String.format("%s/songs/%s/album/%s", url, id, album),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }
        if(!singer.isEmpty() && Integer.valueOf(singer) > 0){
            ResponseEntity res = this.restTemplate.exchange(String.format("%s/songs/%s/singer/%s", url, id, singer),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }

        return view;
    }
    @RequestMapping(value = "/songs/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteSong(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("songs");
        }
        ModelAndView view = new ModelAndView("redirect:/songs");
        String url = getInstancesRun();
        log.info("Deleting Song from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/songs/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted song with ID: " + id :
                "Some error when delete song with ID:" + id;

        view.addObject("result", result);
        return view;
    }


    /////////

    @GetMapping("/albums")
    public ModelAndView getAlbumsView(){
        ModelAndView model = new ModelAndView("albumAll");
        String url = getInstancesRun();
        log.info("Getting all albums from " + url);
        try {
            List<Album> albums = restTemplate.getForObject(String.format("%s/albums", url), List.class);
            List<Singer> singers = restTemplate.getForObject(String.format("%s/singers", url), List.class);
            singers.add(0, null);
            model.addObject("AlbumList", albums);
            model.addObject("Singers", singers);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getAlbum(@PathVariable Long id) {
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("albumDetail");
        Album object = restTemplate.getForObject(String.format("%s/albums/%s", url, id), Album.class);
        List<Singer> singers = restTemplate.getForObject(String.format("%s/singers", url), List.class);
        singers.add(null);
        if(object.getId() == 0)
            view.addObject("error", "We have not album with id: #" + id );
        else
            view.addObject("Album", object);
        view.addObject("Singers", singers);
        return view;
    }
    @RequestMapping(value = "/albums", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createAlbum(@RequestParam("title")String title,
                                    @RequestParam("genre")String genre,
                                    @RequestParam("singer") String singer) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("albums");
        }
        ModelAndView view = new ModelAndView("redirect:/albums");
        String url = getInstancesRun();
        log.info("Posting album");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Album> entity = new HttpEntity<>(new Album(title, genre), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/albums", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        try{
            Album[] ar = restTemplate.getForObject(String.format("%s/albums", url), Album[].class);
            if(!singer.isEmpty() && Integer.valueOf(singer) > 0){
                ResponseEntity res = this.restTemplate.exchange(String.format("%s/albums/%s/singer/%s", url, ar[ar.length-1].getId(), singer),
                        HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                        });
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return view;
    }

    @RequestMapping(value = "/albums/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateAlbum(@RequestParam("title")String title,
                                    @RequestParam("genre")String genre,
                                    @RequestParam("singer") String singer,
                                    @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("albums");
        }
        ModelAndView view = new ModelAndView("redirect:/albums/" + id);
        String url = getInstancesRun();
        log.info("Updating album from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Album> entity = new HttpEntity<>(new Album(title, genre), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/albums/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        if(!singer.isEmpty() && Integer.valueOf(singer) > 0){
            ResponseEntity res = this.restTemplate.exchange(String.format("%s/albums/%s/singer/%s", url, id, singer),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }

        return view;
    }
    @RequestMapping(value = "/albums/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteAlbum(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("albums");
        }
        String url = getInstancesRun();
        log.info("Deleting album from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/albums/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted album with ID: " + id :
                "Some error when delete album with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/albums");
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/albums/{albumId}/removeSong/{songId}")
    public ModelAndView removeSongFromAlbum(@PathVariable int albumId, @PathVariable int songId){
        String url = getInstancesRun();
        Album album = restTemplate.getForObject(String.format("%s/albums/%s", url, albumId), Album.class);
        Song song = restTemplate.getForObject(String.format("%s/songs/%s", url, songId), Song.class);
        if(album.getId() > 0 && song.getId() > 0 )
        {
            ResponseEntity response = this.restTemplate.exchange(String.format("%s/albums/%s/songs/%s", url, album.getId(), song.getId()),
                    HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                    }, getUsername());
            System.out.println(response.toString());
        }

        return new ModelAndView("redirect:/albums/" + albumId);
    }


    //////////////////

    @GetMapping("/singers")
    public ModelAndView getSingersView(){
        ModelAndView model = new ModelAndView("singerAll");
        try{
            String url = getInstancesRun();
            log.info("Getting all Singers from " + url);
            List<Singer> singers = restTemplate.getForObject(String.format("%s/singers", url), List.class);
            model.addObject("SingerList", singers);
            String labelUrl = getLabelInstancesRun();
            List<Producer> producers = restTemplate.getForObject(String.format("%s/producers", labelUrl), List.class);
            producers.add(0, null);
            model.addObject("Producers", producers);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/singers/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getSinger(@PathVariable Long id) {
        ModelAndView view = new ModelAndView("singerDetail");
        try{
            String url = getInstancesRun();
            Singer object = restTemplate.getForObject(String.format("%s/singers/%s", url, id), Singer.class);

            String labelUrl = getLabelInstancesRun();
            if(object.getId() == 0)
                view.addObject("error", "We have not singer with id: #" + id );
            else
                view.addObject("Singer", object);
            try{
                List<Producer> producers = restTemplate.getForObject(String.format("%s/producers", labelUrl), List.class);
                producers.add(0, null);
                view.addObject("Producers", producers);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return view;
    }
    @RequestMapping(value = "/singers", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createSinger(@RequestParam("singername")String singername,
                                     @RequestParam("singerage") int singerage,
                                     @RequestParam("producer") String producer) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("singers");
        }
        ModelAndView view = new ModelAndView("redirect:/singers");
        String url = getInstancesRun();
        log.info("Posting Singer from json ");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Singer> entity = new HttpEntity<>(new Singer(singername, singerage), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });

        Singer[] ar = restTemplate.getForObject(String.format("%s/singers", url), Singer[].class);
        if(!producer.isEmpty() && Integer.valueOf(producer) > 0){
            ResponseEntity res = this.restTemplate.exchange(String.format("%s/singers/%s/producer/%s", url, ar[ar.length-1].getId(), producer),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }

        return view;
    }
    @RequestMapping(value = "/singers/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateSinger(@RequestParam("singername")String singername,
                                     @RequestParam("singerage") int singerage,
                                     @RequestParam("producer") String producer,
                                     @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("singers");
        }
        ModelAndView view = new ModelAndView("redirect:/singers/" + id);
        String url = getInstancesRun();
        log.info("Updating singer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Singer> entity = new HttpEntity<>(new Singer(singername, singerage), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        if(!producer.isEmpty() && Integer.valueOf(producer) > 0){
            ResponseEntity res = this.restTemplate.exchange(String.format("%s/singers/%s/producer/%s", url, id, producer),
                    HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                    });
        }

        return view;
    }
    @RequestMapping(value = "/singers/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteSinger(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("singers");
        }
        ModelAndView view = new ModelAndView("redirect:/singers");
        String url = getInstancesRun();
        log.info("Deleting Singer from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted singer with ID: " + id :
                "Some error when delete singer with ID:" + id;
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/singers/{singerId}/removeSong/{songId}")
    public ModelAndView removeSongFromSinger(@PathVariable int singerId, @PathVariable int songId){
        String url = getInstancesRun();
        Singer singer = restTemplate.getForObject(String.format("%s/singers/%s", url, singerId), Singer.class);
        Song song = restTemplate.getForObject(String.format("%s/songs/%s", url, songId), Song.class);
        if(singer.getId() > 0 && song.getId() > 0 )
        {
            ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s/songs/%s", url, singer.getId(), song.getId()),
                    HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                    }, getUsername());
            System.out.println(response.toString());
        }

        return new ModelAndView("redirect:/singers/" + singerId);
    }
    @GetMapping("/singers/{singerId}/removeAlbum/{albumId}")
    public ModelAndView removeAlbumFromSinger(@PathVariable int singerId, @PathVariable int albumId){
        String url = getInstancesRun();
        Singer singer = restTemplate.getForObject(String.format("%s/singers/%s", url, singerId), Singer.class);
        Album album = restTemplate.getForObject(String.format("%s/albums/%s", url, albumId), Album.class);
        if(singer.getId() > 0 && album.getId() > 0 )
        {
            ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s/albums/%s", url, singer.getId(), album.getId()),
                    HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                    }, getUsername());
            System.out.println(response.toString());
        }

        return new ModelAndView("redirect:/singers/" + singerId);
    }

    /////////////////producers
    @GetMapping("/producers")
    public ModelAndView getProducersView(){
        ModelAndView model = new ModelAndView("producerAll");
        String url = getInstancesRun();
        String labelUrl = getLabelInstancesRun();
        log.info("Getting all Producers from " + url);
        List<Producer> producers = restTemplate.getForObject(String.format("%s/producers", labelUrl), List.class);
        model.addObject("ProducerList", producers);
        return model;
    }
    @RequestMapping(value = "/producers/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getProducer(@PathVariable Long id) {
        String url = getLabelInstancesRun();
        ModelAndView view = new ModelAndView("producerDetail");
        Producer object = restTemplate.getForObject(String.format("%s/producers/%s", url, id), Producer.class);
        if(object.getId() == 0){
            view.addObject("error", "We have not producer with id: #" + id );
            sendMessage("Producer", "Error when Getting Producer #", id, HttpMethod.GET, HttpStatus.NOT_FOUND.toString(), "Not found");
        }

        else
            view.addObject("Producer", object);
        view.addObject("Males", Male.values());
        return view;
    }
    @RequestMapping(value = "/producers", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createProducer(@RequestParam("fullName")String fullName,
                                       @RequestParam("male")Male male,
                                       @RequestParam("age")Integer age,
                                       @RequestParam("experience")Integer experience) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("producers");
        }
        ModelAndView view = new ModelAndView("redirect:/producers");
        try{
            String url = getLabelInstancesRun();
            log.info("Posting Producer from json from " + url );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Producer> entity = new HttpEntity<>(new Producer(fullName, age, experience, male), headers);

            ResponseEntity response = this.restTemplate.exchange(String.format("%s/producers", url),
                    HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                    });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return view;
    }
    @RequestMapping(value = "/producers/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateProducer(@RequestParam("fullName")String fullName,
                                       @RequestParam("male")Male male,
                                       @RequestParam("age")Integer age,
                                       @RequestParam("experience")Integer experience,
                                       @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("producers");
        }
        ModelAndView view = new ModelAndView("redirect:/producers/" + id);
        String url = getLabelInstancesRun();
        log.info("Updating Producer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Producer> entity = new HttpEntity<>(new Producer(fullName, age, experience, male), headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/producers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        return view;
    }
    @RequestMapping(value = "/producers/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteProducer(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("producers");
        }
        ModelAndView view = new ModelAndView("redirect:/producers");
        String url = getLabelInstancesRun();
        log.info("Deleting Producer from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/producers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted producer with ID: " + id :
                "Some error when delete producer with ID:" + id;
        view.addObject("result", result);
        return view;
    }

    @GetMapping("/producers/{producerId}/removeSinger/{singerId}")
    public ModelAndView removeSingerFromProducer(@PathVariable int producerId, @PathVariable int singerId){
        Singer singer = restTemplate.getForObject(String.format("%s/singers/%s", getInstancesRun(), singerId), Singer.class);
        Producer album = restTemplate.getForObject(String.format("%s/producers/%s", getLabelInstancesRun(), producerId), Producer.class);
        if(singer.getId() > 0 && album.getId() > 0 )
        {
            ResponseEntity response = this.restTemplate.exchange(String.format("%s/producers/%s/singers/%s", getLabelInstancesRun(), singer.getId(), album.getId()),
                    HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                    }, getUsername());
            System.out.println(response.toString());
        }

        return new ModelAndView("redirect:/producers/" + producerId);
    }


    //managers
    @GetMapping("/managers")
    public ModelAndView getManagers(){
        String url = getLabelInstancesRun();
        log.info("Getting all managers from " + url);
        List<Manager> managers = restTemplate.getForObject(String.format("%s/managers", url), List.class);
        ModelAndView model = new ModelAndView("managerAll");
        model.addObject("ManagerList", managers);
        model.addObject("ManagerTypes", ManagerTypes.values());
        return model;
    }
    @RequestMapping(value = "/managers/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getManager(@PathVariable Long id) {
        String url = getLabelInstancesRun();
        ModelAndView view = new ModelAndView("managerDetail");
        Manager object = restTemplate.getForObject(String.format("%s/managers/%s", url, id), Manager.class);
        if(object.getId() == 0){
            view = new ModelAndView("redirect:/managers");
            view.addObject("result", "We have not Manager with id: #" + id );
        }
        else{
            view = new ModelAndView("managerDetail");
            view.addObject("Manager", object);
        }
        view.addObject("ManagerTypes", ManagerTypes.values());
        return view;
    }
    @RequestMapping(value = "/managers", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createManager(@ModelAttribute Manager object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("managers");
        }
        String url = getLabelInstancesRun();
        log.info("Posting managers from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Manager> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/managers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/managers");
        return view;
    }
    @RequestMapping(value = "/managers/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateManager(@ModelAttribute Manager object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("managers");
        }
        String url = getLabelInstancesRun();
        log.info("Updating managers from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Manager> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/managers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        Manager group = restTemplate.getForObject(String.format("%s/managers/%s", url, id), Manager.class);

        return getManager(id);
    }
    @RequestMapping(value = "/managers/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteManager(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("managers");
        }
        String url = getLabelInstancesRun();
        log.info("Deleting managers from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/managers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted manager with ID: " + id :
                "Some error when delete manager with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/managers");
        view.addObject("result", result);
        return view;
    }

    //

    @GetMapping("/recordingStudios")
    public ModelAndView getRecordingStudies(){
        ModelAndView model = new ModelAndView("recordingStudioAll");
        String url = getLabelInstancesRun();
        log.info("Getting all recordingStudios from " + url);
        try{
            List<RecordingStudio> recordingStudios = restTemplate.getForObject(String.format("%s/recordingStudios", url), List.class);

            model.addObject("RecordingStudioList", recordingStudios);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return model;
    }
    @RequestMapping(value = "/recordingStudies/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView getRecordingStudio(@PathVariable Long id) {
        String url = getLabelInstancesRun();
        ModelAndView view = null;
        RecordingStudio object = restTemplate.getForObject(String.format("%s/recordingStudios/%s", url, id), RecordingStudio.class);
        if(object.getId() == 0){
            view = new ModelAndView("redirect:/recordingStudios");
            view.addObject("result", "We have not RecordingStudio with id: #" + id );
        }
        else{
            view = new ModelAndView("recordingStudioDetail");
            view.addObject("RecordingStudio", object);
        }
        return view;
    }
    @RequestMapping(value = "/recordingStudies", method = RequestMethod.POST, produces="application/json")
    public ModelAndView createRecordingStudio(@ModelAttribute RecordingStudio object) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("recordingStudios");
        }
        String url = getLabelInstancesRun();
        log.info("Posting recordingStudios from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RecordingStudio> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/recordingStudios", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        ModelAndView view = new ModelAndView("redirect:/recordingStudios");
        return view;
    }
    @RequestMapping(value = "/recordingStudies/{id}", method = RequestMethod.POST, produces="application/json")
    public ModelAndView updateRecordingStudio(@ModelAttribute RecordingStudio object, @PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("recordingStudios");
        }
        String url = getLabelInstancesRun();
        log.info("Updating recordingStudios from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RecordingStudio> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/recordingStudios/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);
        RecordingStudio group = restTemplate.getForObject(String.format("%s/recordingStudios/%s", url, id), RecordingStudio.class);

        return getRecordingStudio(id);
    }
    @RequestMapping(value = "/recordingStudies/delete/{id}", method = RequestMethod.GET, produces="application/json")
    public ModelAndView deleteRecordingStudio(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("recordingStudios");
        }
        String url = getLabelInstancesRun();
        log.info("Deleting recordingStudios from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/recordingStudios/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted recordingStudio with ID: " + id :
                "Some error when delete recordingStudio with ID:" + id;
        ModelAndView view = new ModelAndView("redirect:/recordingStudios");
        view.addObject("result", result);
        return view;
    }



    ///////////////////
    //
    @GetMapping("/messages")
    public ModelAndView getMessages(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        String url = getInstancesRun();
        log.info("Getting all messages from " + url);
        List<Message> messages = restTemplate.getForObject(String.format("%s/messages", url), List.class);
        ModelAndView model = new ModelAndView("messageAll");
        model.addObject("messageList", messages);
        return model;
    }
    @RequestMapping(value = "/messages/{id}", method = RequestMethod.GET)
    public ModelAndView getMessage(@PathVariable Long id) {
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        String url = getInstancesRun();
        ModelAndView view = new ModelAndView("messageDetail");
        Message message = restTemplate.getForObject(String.format("%s/messages/%s", url, id), Message.class);
        if(message.getId() == 0)
            view.addObject("error", "We have not message with id: #" + id );
        else
            view.addObject("Message", message);
        return view;
    }

    @GetMapping("/allProperties")
    public ModelAndView getPropertiesFromAllModules(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        ModelAndView view = new ModelAndView("propertiesAll");
        try {
            String schoolServiceProps = restTemplate.exchange(String.format("%s/properties", getInstancesRun()),
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("school", schoolServiceProps);
        }
        catch (Exception ex){
            log.info("Error on MUSIC SERVICE");
            log.info(ex.getMessage());
        }
        try{
            String lessonServiceProps = restTemplate.exchange(String.format("%s/properties", getLabelInstancesRun()),
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("lesson", lessonServiceProps);
        }
        catch (Exception ex){
            log.info("Error on LABEL SERVICE");
            log.info(ex.getMessage());
        }
        try{
            String url = getInstancesRun();
            String serverProps = restTemplate.exchange("http://localhost:7777/properties",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }).getBody();
            view.addObject("server", serverProps);
        }
        catch (Exception ex){
            log.info("Error on SERVER");
            log.info(ex.getMessage());
        }
        try {
            view.addObject("client", getPropertiesClient());
        }
        catch (JsonProcessingException e) {
            log.info("Error on CLIENT");
            log.info(e.getMessage());
        }
        return view;
    }
    @PostMapping("/updateAllProperties")
    public ModelAndView updateAllProperties(){
        ResponseEntity response = restTemplate.exchange("http://localhost:7777/actuator/bus-refresh",
                HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                });

        ModelAndView view = new ModelAndView("redirect:/allProperties");
        if(response.getStatusCode() == HttpStatus.NO_CONTENT){
            view.addObject("result", "Properties are updated!");
            sendMessage("Admin", "Updated all configs", 0l, HttpMethod.POST, HttpStatus.OK.toString(), "No error");
        }
        else
        {
            view.addObject("result", "Something wrong");
            sendMessage("Admin", "Fail when update configs\r\n" + response, 0l, HttpMethod.POST, HttpStatus.INTERNAL_SERVER_ERROR.toString(), response.getBody().toString());
        }
        return view;
    }

    @GetMapping("/admins")
    public ModelAndView getAdminPage(){
        if(!isAdmin()){
            return redirectIfHaveNotAccess("");
        }
        return new ModelAndView("createAdmin");
    }
    @PostMapping("/admins")
    public ModelAndView createAdmin(@ModelAttribute User user, String role){
        user.setEnabled(true);
        userRep.save(user);
        Authorities a = new Authorities(user.getUsername(),"ROLE_"+role.toUpperCase());
        roleRep.save(a);
        return new ModelAndView("redirect:/login");
    }
    private ModelAndView redirectIfHaveNotAccess(String viewName){
        return redirectIfHaveNotAccess(new ModelAndView("redirect:/" + viewName));
    }
    private ModelAndView redirectIfHaveNotAccess(ModelAndView redirectDesination){
        if(!isAdmin()){
            ModelAndView view = redirectDesination;
            view.addObject("result", getWrongAccessMessage());
            return view;
        }
        return new ModelAndView("/");
    }
    private String getWrongAccessMessage(){
        return "Dear, " + getUsername() + "." +
                "<br>Unfortunately u have not access to do this :c.<br>But u always can upgrade yourself ;-)";
    }
    private boolean isAdmin(){
        return isHasRole("admin");
    }
    private boolean isHasRole(String role){
        if(!role.equalsIgnoreCase("user") &&!role.equalsIgnoreCase("admin"))
            return false;
        Collection<? extends GrantedAuthority> list = getAuthentication().getAuthorities();
        for (GrantedAuthority au: list) {
            if(au.getAuthority().equalsIgnoreCase("role_" + role))
                return true;
        }
        return false;
    }
    private String getUsername(){
        return getAuthentication().getName();
    }
    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }



    private void sendMessage(Message message){
        producer.sendMessage(message);
    }
    private void sendMessage(Class clas, ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        sendMessage(clas.getName(), response, id, httpMethod, error);
    }
    private void sendMessage(String classname, ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendMessage(
                new Message(
                        classname,
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        error
                )
        );
    }
    private void sendMessage(String classname, String response, Long id, HttpMethod httpMethod, String status, String error){
        producer.sendMessage(
                new Message(
                        classname,
                        response,
                        httpMethod,
                        status,
                        error
                )
        );
    }
}
