package com.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@SpringBootApplication// (exclude = {ErrorMvcAutoConfiguration.class})
@RefreshScope
@Configuration
public class EurekaClientLab2Application {
    @Value("${queue.singer.name}")
    private String qSinger;

    @Value("${queue.song.name}")
    private String qSong;

    @Value("${queue.album.name}")
    private String qAlbum;

    @Value("${queue.producer.name}")
    private String qProducer;

    @Value("${spring.rabbitmq.host}")
    private String brokerUrl;

    @Value("${topic.exchange.name}")
    private String topicName;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String pwd;

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientLab2Application.class, args);
	}

    @Bean(name ="queueSinger")
    public Queue queueSinger() {
        return new Queue(qSinger, true);
    }

    @Bean(name="exchangeSinger")
    public TopicExchange exchangeSinger() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingSinger")
    public Binding bindingCustomer(Queue queueSinger, TopicExchange exchangeSinger) {
        return BindingBuilder.bind(queueSinger).to(exchangeSinger).with(qSinger);
    }

    @Bean(name="queueSong")
    public Queue queueSong() {
        return new Queue(qSong, true);
    }

    @Bean(name="exchangeSong")
    public TopicExchange exchangeSong() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingSong")
    public Binding bindingShop(Queue queueSong, TopicExchange exchangeSong) {
        return BindingBuilder.bind(queueSong).to(exchangeSong).with(qSong);
    }

    @Bean(name="queueAlbum")
    public Queue queueAlbum() {
        return new Queue(qAlbum, true);
    }

    @Bean(name="exchangeAlbum")
    public TopicExchange exchangeAlbum() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingAlbum")
    public Binding bindingShop_Album(Queue queueAlbum, TopicExchange exchangeAlbum) {
        return BindingBuilder.bind(queueAlbum).to(exchangeAlbum).with(qAlbum);
    }

    @Bean(name="queueProducer")
    public Queue queueProducer() {
        return new Queue(qAlbum, true);
    }

    @Bean(name="exchangeProducer")
    public TopicExchange exchangeProducer() {
        return new TopicExchange(topicName);
    }

    @Bean(name="bindingProducer")
    public Binding binding_Shop_Producer(Queue queueProducer, TopicExchange exchangeProducer) {
        return BindingBuilder.bind(queueProducer).to(exchangeProducer).with(qProducer);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(brokerUrl);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(pwd);

        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name="rabbitTemplateSinger")
    @Primary
    public RabbitTemplate rabbitTemplateSinger() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qSinger);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="rabbitTemplateSong")
    public RabbitTemplate rabbitTemplateSong() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qSong);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="rabbitTemplateAlbum")
    public RabbitTemplate rabbitTemplateAlbum() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qAlbum);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean(name="rabbitTemplateProducer")
    public RabbitTemplate rabbitTemplateProducer() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(qProducer);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
