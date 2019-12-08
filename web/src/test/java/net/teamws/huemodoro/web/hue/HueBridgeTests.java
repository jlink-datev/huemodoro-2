package net.teamws.huemodoro.web.hue;

import org.intellij.lang.annotations.*;
import org.junit.jupiter.api.*;
import org.springframework.test.web.client.*;
import org.springframework.web.client.*;

import net.teamws.huemodoro.web.*;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class HueBridgeTests {

	private HueBridge hueBridge;
	private MockRestServiceServer server;

	@BeforeEach
	void setupServerMock() {
		HueConfiguration hueConfiguration = new HueConfiguration();
		hueConfiguration.setHost("18.194.37.236");
		hueConfiguration.setClient("3O2Ce9zBt9ed7Ht9kdvAlYVbLr7ycmVtTOc6FICU");
		hueBridge = new HueBridge(hueConfiguration);

		RestTemplate restTemplate = new RestTemplate();
		server = MockRestServiceServer.bindTo(restTemplate).build();
		hueBridge.setRestTemplate(restTemplate);

	}

	@Test
	void itShouldTurnOffLight() {
		@Language("json") String json = "{\n" +
												"\"on\": false" +
												"}";
		expectedJsonPayload(json);

		hueBridge.lightOff();

		server.verify();
	}

	@Test
	void itShouldCallRed() {
		@Language("json") String json = "{\n" +
												"\"on\": true\n," +
												"\"hue\": 1\n" +
												"}";
		expectedJsonPayload(json);

		hueBridge.lightOn(Colour.RED);

		server.verify();
	}

	@Test
	void itShouldCallGreen() {
		@Language("json") String json = "{\n" +
												"\"on\": true\n," +
												"\"hue\": 23000\n" +
												"}";
		expectedJsonPayload(json);

		hueBridge.lightOn(Colour.GREEN);

		server.verify();
	}

	private void expectedJsonPayload(String expectedJsonContent) {
		expectedUrl()
				.andExpect(content().json(expectedJsonContent))
				.andRespond(withSuccess());
	}

	private ResponseActions expectedUrl() {
		return server.expect(requestTo(
				"http://"
						+ "18.194.37.236"
						+ "/api/"
						+ "3O2Ce9zBt9ed7Ht9kdvAlYVbLr7ycmVtTOc6FICU"
						+ "/lights/"
						+ "1"
						+ "/state"))
					 .andExpect(method(PUT));
	}
}