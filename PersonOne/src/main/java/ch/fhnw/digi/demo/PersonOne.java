package ch.fhnw.digi.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PersonOne {

	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder ctx = new SpringApplicationBuilder(PersonOne.class);
		// as we are creating a basic gui we need to deactivate the headless mode
		ctx.headless(false).run(args);

	}

}
