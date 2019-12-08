package net.teamws.huemodoro.web;

import java.time.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import net.teamws.huemodoro.web.repository.*;

class AdvanceTimeSchedulerTests {

	@Test
	void advanceTimeInSessionRepositoryByOneSecond() {
		SessionRepository repository = Mockito.mock(SessionRepository.class);

		AdvanceTimeScheduler scheduler = new AdvanceTimeScheduler(repository);

		scheduler.advanceTime();
		scheduler.advanceTime();
		scheduler.advanceTime();

		Mockito.verify(repository, Mockito.times(3)).advanceTime(Duration.ofSeconds(1));
	}
}
