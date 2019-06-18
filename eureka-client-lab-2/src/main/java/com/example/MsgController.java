

package com.example;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collections;

/*
@RestController
@EnableDiscoveryClient
@RequestMapping("/api")
public class MsgController {

    @Autowired
    private MsgProducer producer;

    private static Logger log = LoggerFactory.getLogger(EurekaClientLab2Application.class);

    private final RestTemplate restTemplate;

    @Autowired
    public MsgController(RestTemplateBuilder restTemplateBuilder,
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

//    @PostMapping(value = "/refreshing", produces = "application/json; charset=UTF-8")
//    public String checkRefresh() throws JsonProcessingException
//    {
//        return refresh() + getPropertiesClient();
//    }
//
//    @PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
//    public String refresh()
//    {
//        return "Refreshed";
//    }
//
//    @GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
//    public String getPropertiesClient() throws JsonProcessingException
//    {
//        Map<String, Object> props = new HashMap<>();
//        CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
//        for (String propertyName : bootstrapProperties.getPropertyNames()) {
//            props.put(propertyName, bootstrapProperties.getProperty(propertyName));
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        return mapper.writeValueAsString(props);
//    }

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


    //singers
    @RequestMapping(value = "/singers/{id}", method = RequestMethod.GET, produces = "application/json")
    public String getSinger(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getInstancesRun();
        log.info("Getting all details for singer " + id + " from " + url);
        ResponseEntity response = restTemplate.exchange(String.format("%s/singers/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Singer.class, id);

        log.info("Info about singer: " + id);

        sendSingerMessage(response, id, HttpMethod.GET, response.getBody().toString());
        return response.getBody().toString();
    }
    @RequestMapping(value = "/singers", method = RequestMethod.GET, produces="application/json")
    public String getSingers() {
        String url = getInstancesRun();
        log.info("Getting all singers" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/singers", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All singers: \n" + response;
    }
    @RequestMapping(value = "/singers", method = RequestMethod.POST, produces="application/json")
    public String createSinger(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting singer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendSingerMessage(response, new Long(0), HttpMethod.POST, response.getBody().toString());
        return "Posted singer: \n" + response.getBody();
    }
    @RequestMapping(value = "/singers/{id}", method = RequestMethod.PUT, produces="application/json")
    public String updateSinger(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating singer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        sendSingerMessage(response, id, HttpMethod.PUT, response.getBody().toString());
        return "Updated singer: \n" + response.getBody();
    }
    @RequestMapping(value = "/singers/{id}", method = RequestMethod.DELETE*/
/*, produces="application/json"*//*
)
    public String deleteSinger(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting singer from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted singer with ID: " + id :
                "Some error when delete singer with ID:" + id;
        sendSingerMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return result;//"{\"result\":\"" + result + "\"}";
    }
    @RequestMapping(value = "/singers/{id}/producers", method = RequestMethod.GET, produces = "application/json")
    public String getSingerProducers(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getInstancesRun();
        log.info("Getting all producers for singer " + id + " from " + url);

        String response = this.restTemplate.exchange(String.format("%s/singers/%s/producers", url, Long.toString(id)),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "Producers of singer with id " + id + "ID:" + response;
    }
    @RequestMapping(value = "/singers/{id}/producers/{producerId}", method = RequestMethod.DELETE*/
/*, produces="application/json"*//*
)
    public String removeProducerFromSinger(@PathVariable Long id, @PathVariable Long producerId) {
        String url = getInstancesRun();
        log.info("Deleting producer " + producerId + "# from singer " + id + "# from: " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/singers/%s/producers/%s", url, id, producerId),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);
        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully removed producer " + producerId + "# from singer " + id + "#" :
                "Some error when remove producer " + producerId + "# from singer " + id + "#" ;
        sendSingerMessage(
                response,
                id,
                HttpMethod.DELETE,
                result);
        return result;//"{\"result\":\"" + result + "\"}";
    }


    //songs
    @RequestMapping(value = "/songs/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getSong(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity httpEntity = new HttpEntity(headers);
        String url = getInstancesRun();
        log.info("Getting all details for song " + id + " from " + url);
        ResponseEntity response =  restTemplate.exchange(String.format("%s/songs/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Song.class, id);

        log.info("Info about song: " + id);

        sendSongMessage(response, id, HttpMethod.GET, response.getBody().toString());

        return response.getBody().toString();
    }
    @RequestMapping(value = "/songs", method = RequestMethod.GET,produces="application/json")
    public String getSongs() {
        String url = getInstancesRun();
        log.info("Getting all songs" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/songs", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All songs: \n" + response;
    }
    @RequestMapping(value = "/songs", method = RequestMethod.POST,produces="application/json")
    public String createSong(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting song from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response =  this.restTemplate.exchange(String.format("%s/songs", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendSongMessage(response, new Long(0), HttpMethod.POST, response.getBody().toString());

        return "Posted song: \n" + response.getBody();
    }
    @RequestMapping(value = "/songs/{id}", method = RequestMethod.PUT,produces="application/json")
    public String updateSong(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating song from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/songs/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);


        sendSongMessage(response, id, HttpMethod.PUT, response.getBody().toString());

        return "Updated song: \n" + response;
    }
    @RequestMapping(value = "/songs/{id}", method = RequestMethod.DELETE,produces="application/json")
    public String deleteSong(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting song from " + url);
        ResponseEntity response = this.restTemplate.exchange(String.format("%s/songs/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);

        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted Song with ID: " + id :
                "Some error when delete Song with ID:" + id;

        sendSongMessage(response, id, HttpMethod.DELETE, result);

        return "Deleted song: \n" + id + "\n" + response.getBody();
    }

    //albums
    @RequestMapping(value = "/album/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getAlbum(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getInstancesRun();
        log.info("Getting all details for album " + id + " from " + url);
        ResponseEntity response = restTemplate.exchange(String.format("%s/albums/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Album.class, id);

        log.info("Info about album: " + id);

        sendAlbumMessage(response, id, HttpMethod.GET, response.getBody().toString());

        return response.getBody().toString();
    }
    @RequestMapping(value = "/albums", method = RequestMethod.GET,produces="application/json")
    public String getAlbums() {
        String url = getInstancesRun();
        log.info("Getting all albums" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/albums", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All albums: \n" + response;
    }
    @RequestMapping(value = "/albums", method = RequestMethod.POST,produces="application/json")
    public String createAlbum(@RequestBody String object) {
        String url = getInstancesRun();
        log.info("Posting album from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response =  this.restTemplate.exchange(String.format("%s/albums", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });
        sendAlbumMessage(response, new Long(0), HttpMethod.POST, response.getBody().toString());

        return "Posted album: \n" + response.getBody();
    }
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.PUT,produces="application/json")
    public String updateAlbum(@RequestBody String object, @PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Updating album from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/albums/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        sendAlbumMessage(response, id, HttpMethod.PUT, response.getBody().toString());

        return "Updated album: \n" + response;
    }
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.DELETE,produces="application/json")
    public String deleteSAlbum(@PathVariable Long id) {
        String url = getInstancesRun();
        log.info("Deleting album from " + url);
        ResponseEntity response =  this.restTemplate.exchange(String.format("%s/albums/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);

        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted Album with ID: " + id :
                "Some error when delete Album with ID:" + id;
        sendAlbumMessage(response, id, HttpMethod.DELETE, result);
        return "Deleted album: \n" + id + "\n" + response.getBody();
    }



    //producers
    @RequestMapping(value = "/producers/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String getProducer(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(headers);

        String url = getLabelInstancesRun();
        log.info("Getting all details for producer " + id + " from " + url);
        ResponseEntity response = restTemplate.exchange(String.format("%s/producers/%s", url, Long.toString(id)),
                HttpMethod.GET, httpEntity, Producer.class, id);

        log.info("Info about producer: " + id);

        sendProducerMessage(response, id, HttpMethod.GET, response.getBody().toString());

        return response.getBody().toString();
    }
    @RequestMapping(value = "/producers", method = RequestMethod.GET,produces="application/json")
    public String getProducers() {
        String url = getLabelInstancesRun();
        log.info("Getting all producers" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/producers", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All producers: \n" + response;
    }
    @RequestMapping(value = "/producers", method = RequestMethod.POST,produces="application/json")
    public String createProducer(@RequestBody String object) {
        String url = getLabelInstancesRun();
        log.info("Posting producer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response =  this.restTemplate.exchange(String.format("%s/producers", url),
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });

        sendProducerMessage(response, new Long(0), HttpMethod.POST, response.getBody().toString());

        return "Posted producer: \n" + response.getBody();
    }
    @RequestMapping(value = "/producers/{id}", method = RequestMethod.PUT,produces="application/json")
    public String updateProducer(@RequestBody String object, @PathVariable Long id) {
        String url = getLabelInstancesRun();
        log.info("Updating producer from json from " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(object, headers);

        ResponseEntity response = this.restTemplate.exchange(String.format("%s/producers/%s", url, id),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }, id);

        sendProducerMessage(response, id, HttpMethod.PUT, response.getBody().toString());

        return "Updated producer: \n" + response;
    }
    @RequestMapping(value = "/producers/{id}", method = RequestMethod.DELETE,produces="application/json")
    public String deleteProducer(@PathVariable Long id) {
        String url = getLabelInstancesRun();
        log.info("Deleting producer from " + url);
        ResponseEntity response =  this.restTemplate.exchange(String.format("%s/producers/%s", url, id),
                HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                }, id);

        String result = response.getStatusCode() == HttpStatus.OK ?
                "Successfully deleted Producer with ID: " + id :
                "Some error when delete Producer with ID:" + id;
        sendProducerMessage(response, id, HttpMethod.DELETE, result);

        return "Deleted producer: \n" + id + "\n" + response.getBody();
    }


    @RequestMapping(value="/info-producer",method=RequestMethod.GET,produces="application/json")
    public String info()
    {
        ObjectNode root = producer.info();

        return root.toString();
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET,produces="application/json")
    public String getMessages() {
        String url = getInstancesRun();
        log.info("Getting all messages" + " from " + url);
        String response = this.restTemplate.exchange(String.format("%s/messages", url),
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }).getBody();

        return "All messages: \n" + response;
    }

    private void sendSingerMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendSingerMsg(
                new SingerMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
    private void sendSongMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendSongMsg(
                new SongMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
    private void sendProducerMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error){
        producer.sendProducerMsg(
                new ProducerMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
    private void sendAlbumMessage(ResponseEntity response, Long id, HttpMethod httpMethod, String error) {
        producer.sendAlbumMsg(
                new AlbumMessage(
                        "ID: " + (id == 0 ? "not have ID yet" : id) + ". " + response.getStatusCode().getReasonPhrase() + ". Is error: " + response.getStatusCode().isError(),
                        httpMethod,
                        response.getStatusCode().toString(),
                        new Timestamp(System.currentTimeMillis()),
                        error
                )
        );
    }
}

*/
