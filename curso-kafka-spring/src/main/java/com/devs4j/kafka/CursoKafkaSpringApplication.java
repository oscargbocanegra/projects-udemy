package com.devs4j.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@SpringBootApplication
public class CursoKafkaSpringApplication implements CommandLineRunner{
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(CursoKafkaSpringApplication.class);

	@KafkaListener(topics = "desvs4j-topic", containerFactory = "listenerContainerFactory", groupId = "desvs4j",
			properties = {"max.poll.interval.ms:40000","max.poll.records:10"})
	public void listen(List<ConsumerRecord<String, String>> messages) {
		log.info("Start reading messages");
		for (ConsumerRecord<String, String> message: messages) {
			log.info("Partition = {}, Offset ={}, Key ={}, Value ={} " , message.partition(), message.offset(), message.key(), message.value());
		}
		log.info("Batch complete");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CursoKafkaSpringApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
		for (int i=0; i<100; i++) {
			kafkaTemplate.send("desvs4j-topic", String.valueOf(i) ,String.format("Sample message %d", i));
		}
	}
	
	/**--Metodo Usando Callback
	@Override
	public void run(String...args) throws Exception {
		
		ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send("desvs4j-topic", "Sample message");
		future.addCallback(new KafkaSendCallback<String, String>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("Message sent", result.getRecordMetadata().offset());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("Error sending message ", ex);
				
			}

			@Override
			public void onFailure(KafkaProducerException ex) {
				log.error("Error sending message ", ex);
				
			}
		});
	}
	**/
}
