package com.devs4j.kafka.transactional;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalProducer {
	
	public static final Logger log = LoggerFactory.getLogger(TransactionalProducer.class);
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		Properties props=new Properties();
		props.put("bootstrap.servers","localhost:9092"); // Broker de kafka a conectar
		props.put("acks","all"); // valores 0=Sin importar 1=minimo 1 nodo, all = todos los nodos confirman mensaje recibido.
		props.put("transactional.id", "desvs4j-producer-id");
		props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("linger.ms","1");
				
		try (Producer<String, String> producer = new KafkaProducer<>(props);){
			try {
				producer.initTransactions();
				producer.beginTransaction();
				for (int i=0; i<1000; i++) {
				producer.send(new ProducerRecord<String, String>("desvs4j-topic", String.valueOf(i),"desvs4j-value"));
//				if(i==500) {
//					throw new Exception("Unexpected Exception");
//				}
				}
				producer.commitTransaction();
				producer.flush();
			}catch(Exception e) {
				log.error("Error", e);
				producer.abortTransaction();
				
			}
			
		}
		log.info("Procesing time = {} ", (System.currentTimeMillis() - startTime));
	}
}
