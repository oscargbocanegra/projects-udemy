package com.devs4j.kafka.callbacks;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Devs4jCallbackProducer {
	
	public static final Logger log = LoggerFactory.getLogger(Devs4jCallbackProducer.class);
	
public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		Properties props=new Properties();
		props.put("bootstrap.servers","localhost:9092"); // Broker de kafka a conectar
		props.put("acks","1"); // valores 0=Sin importar 1=minimo 1 nodo, all = todos los nodos confirman mensaje recibido.
		props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("linger.ms","10");
				
		try (Producer<String, String> producer = new KafkaProducer<>(props);){
			for (int i=0; i<1000000; i++) {
			producer.send(new ProducerRecord<String, String>("desvs4j-topic", String.valueOf(i),"desvs4j-value"), new Callback() {
				
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if(exception != null) {
						log.info("There was an error {} ", exception.getMessage());
					}
					log.info("Offset = {}, Partition = {}, Topic= {} ", metadata.offset(),
							metadata.partition(),metadata.topic());
				}
			});
			}
			producer.flush();
		}
		log.info("Procesing time = {} ", (System.currentTimeMillis() - startTime));
	}
}
