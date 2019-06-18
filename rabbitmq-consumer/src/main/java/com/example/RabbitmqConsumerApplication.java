package com.example;

import com.example.listeners.impl.ListenerAlbum;
import com.example.listeners.impl.ListenerProducer;
import com.example.listeners.impl.ListenerSinger;
import com.example.listeners.impl.ListenerSong;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class RabbitmqConsumerApplication {
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


	private static final String LISTENER_METHOD = "receiveMessage";

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqConsumerApplication.class, args);
	}

	@Bean(name ="queueSinger")
	Queue queueSinger() {
		return new Queue(qSinger, true);
	}

	@Bean(name="exchangeSinger")
	TopicExchange exchangeSinger() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingSinger")
	Binding bindingSinger(Queue queueSinger, TopicExchange exchangeSinger) {
		return BindingBuilder.bind(queueSinger).to(exchangeSinger).with(qSinger);
	}

	@Bean(name="queueSong")
	Queue queueSong() {
		return new Queue(qSong, true);
	}

	@Bean(name="exchangeSong")
	TopicExchange exchangeSong() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingSong")
	Binding bindingSong(Queue queueSong, TopicExchange exchangeSong) {
		return BindingBuilder.bind(queueSong).to(exchangeSong).with(qSong);
	}

	@Bean(name="queueAlbum")
	Queue queueAlbum() {
		return new Queue(qAlbum, true);
	}

	@Bean(name="exchangeAlbum")
	TopicExchange exchangeAlbum() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingAlbum")
	Binding bindingAlbum(Queue queueAlbum, TopicExchange exchangeAlbum) {
		return BindingBuilder.bind(queueAlbum).to(exchangeAlbum()).with(qAlbum);
	}

	@Bean(name ="queueProducer")
	Queue queueProducer() {
		return new Queue(qProducer, true);
	}

	@Bean(name="exchangeProducer")
	TopicExchange exchangeProducer() {
		return new TopicExchange(topicName);
	}

	@Bean(name="bindingProducer")
	Binding bindingProducer(Queue queueProducer, TopicExchange exchangeProducer) {
		return BindingBuilder.bind(queueProducer).to(exchangeProducer()).with(qProducer);
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

	@Bean(name="containerSinger")
	SimpleMessageListenerContainer containerSinger(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterSinger) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qSinger);
		container.setMessageListener(listenerAdapterSinger);
		return container;
	}

	@Bean(name="listenerAdapterSinger")
	public MessageListenerAdapter listenerAdapterSinger(ListenerSinger receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}

	@Bean(name="containerSong")
	SimpleMessageListenerContainer containerSong(ConnectionFactory connectionFactory,
													 MessageListenerAdapter listenerAdapterSong) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qSong);
		container.setMessageListener(listenerAdapterSong);
		return container;
	}

	@Bean(name="listenerAdapterSong")
	public MessageListenerAdapter listenerAdapterSong(ListenerSong receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}

	@Bean(name="containerAlbum")
	SimpleMessageListenerContainer containerAlbum(ConnectionFactory connectionFactory,
												   MessageListenerAdapter listenerAdapterAlbum) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qSinger);
		container.setMessageListener(listenerAdapterAlbum);
		return container;
	}

	@Bean(name="listenerAdapterAlbum")
	public MessageListenerAdapter listenerAdapterAlbum(ListenerAlbum receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}

	@Bean(name="containerProducer")
	SimpleMessageListenerContainer containerProducer(ConnectionFactory connectionFactory,
												  MessageListenerAdapter listenerAdapterProducer) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setMessageConverter(jsonMessageConverter());
		container.setQueueNames(qProducer);
		container.setMessageListener(listenerAdapterProducer);
		return container;
	}

	@Bean(name="listenerAdapterProducer")
	public MessageListenerAdapter listenerAdapterProducer(ListenerProducer receiver) {
		MessageListenerAdapter msgAdapter = new MessageListenerAdapter(receiver);
		msgAdapter.setMessageConverter(jsonMessageConverter());
		msgAdapter.setDefaultListenerMethod(LISTENER_METHOD);

		return msgAdapter;
	}
}
