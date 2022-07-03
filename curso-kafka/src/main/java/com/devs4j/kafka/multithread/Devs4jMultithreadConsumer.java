package com.devs4j.kafka.multithread;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Devs4jMultithreadConsumer {
	
	public static void main(String[] args) {
		
		Properties props=new Properties();
		props.setProperty("bootstrap.servers","localhost:9092");
		props.setProperty("group.id","devs4j-group");
		props.setProperty("enable.auto.commit","true");
		props.setProperty("auto.offset.reset","earliest");
		props.setProperty("auto.commit.interval.ms","1000");
		props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		ExecutorService executor= Executors.newFixedThreadPool(10);
		for(int i= 0;i< 10;i++) {
			Runnable worker= new Devs4jThreadConsumer(new KafkaConsumer<>(props));
			executor.execute(worker);
			}
		executor.shutdown();
		while(!executor.isTerminated()) ;
		}
	}