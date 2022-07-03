package com.devs4j.kafka.producers;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Devs4jProducers {
	
	public static final Logger log = LoggerFactory.getLogger(Devs4jProducers.class);
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		Properties props=new Properties();
		props.put("bootstrap.servers","localhost:9092"); // Broker de kafka a conectar
		props.put("acks","1"); // valores 0=Sin importar 1=minimo 1 nodo, all = todos los nodos confirman mensaje recibido.
		props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("linger.ms","10");
		//props.put("batch.size","32384");
		//props.put("transactional.id","devs4j-producer");
		//props.put("buffer.memory","33554432");
		//props.put("compression.type","gzip");
				
		// 2.098 ms to send 1.000.000 - linger 0
		// 1.631 ms to send 1.000.000 - linger 6
		// 1.847 ms to send 1.000.000 - linger 4
		// 7.316 ms to send 10.000.000 - linger 10
		// 7.143 ms to send 10.000.000 - linger 0
		// 6.472 ms to send 10.000.000 - linger 15
		// 7.449 ms to send 10.000.000 - linger 25
				
		try (Producer<String, String> producer = new KafkaProducer<>(props);){
			for (int i=0; i<1000000; i++) {
			producer.send(new ProducerRecord<String, String>("desvs4j-topic", String.valueOf(i),"desvs4j-value"));
			}
			producer.flush();
		}
		log.info("Procesing time = {} ", (System.currentTimeMillis() - startTime));
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			log.error("Message producer interruped", e);
//		} 
	}
}
