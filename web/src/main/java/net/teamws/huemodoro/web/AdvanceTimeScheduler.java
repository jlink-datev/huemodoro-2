package net.teamws.huemodoro.web;

import java.time.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import net.teamws.huemodoro.web.repository.*;

@Component
public class AdvanceTimeScheduler {

	private static Logger logger = LoggerFactory.getLogger(AdvanceTimeScheduler.class);

	private SessionRepository repository;

	public AdvanceTimeScheduler(@Autowired SessionRepository repository) {
		this.repository = repository;
	}

	// @Scheduled(fixedRate = 10000)
	public void reportCurrentTime() {
		logger.info("The time is now {}", Instant.now());
	}

	@Scheduled(fixedRate = 1000)
	public void advanceTime() {
		logger.trace("Advancing time");
		repository.advanceTime(Duration.ofSeconds(1));
	}

}
