package ch.fhnw.digi.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class Classifier {

	public static void main(String[] args) {
		SpringApplicationBuilder ctx = new SpringApplicationBuilder(Classifier.class);
		// as we are creating a basic gui we need to deactivate the headless mode
        ctx.headless(false).run(args);
	}

}
