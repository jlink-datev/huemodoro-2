package net.teamws.huemodoro.web;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.scheduling.annotation.*;

@SpringBootApplication
@EnableScheduling
public class HuemodoroApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuemodoroApplication.class, args);
	}
}
