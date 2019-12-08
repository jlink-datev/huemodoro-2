package net.teamws.huemodoro.web.hue;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.context.properties.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

import net.teamws.huemodoro.web.*;

@ContextConfiguration(classes = {HueBridge.class, HueConfiguration.class})
@TestPropertySource(locations = "file:../application.properties")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@Disabled
class HueBridgeConfigurationCheck {

	private static Logger logger = LoggerFactory.getLogger(HueBridgeConfigurationCheck.class);

	@Autowired
	private HueBridge hueBridge;

	@BeforeAll
	static void logConfiguration(@Autowired HueConfiguration configuration) {
		logger.info("Hue host:   {}", configuration.getHost());
		logger.info("Hue client: {}", configuration.getClient());
		logger.info("Hue lamp:   {}", configuration.getLamp());
	}

	@Test
	void redGreenOff() throws InterruptedException {
		hueBridge.lightOn(Colour.RED);
		Thread.sleep(1000);
		hueBridge.lightOn(Colour.GREEN);
		Thread.sleep(1000);
		hueBridge.lightOff();
	}

}