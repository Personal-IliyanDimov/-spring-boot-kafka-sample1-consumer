package org.imd.kafka.sample1.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class Sample1ConsumerApplication {

	public static void main(String[] args) {
		Schedulers.enableMetrics();
		SpringApplication.run(Sample1ConsumerApplication.class, args);
	}

}
