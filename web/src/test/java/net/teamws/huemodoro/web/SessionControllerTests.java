package net.teamws.huemodoro.web;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import net.teamws.huemodoro.domain.*;
import net.teamws.huemodoro.web.repository.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import static net.teamws.huemodoro.domain.SessionState.*;
import static net.teamws.huemodoro.web.sessions.SessionController.*;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {HuemodoroApplication.class})
class SessionControllerTests {

	@MockBean
	SessionRepository sessionRepository;

	private MockMvc mockMvc;

	@BeforeEach
	void setup(@Autowired WebApplicationContext wac) {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	void getSession() throws Exception {
		HuemodoroSession session = new HuemodoroSession();
		when(sessionRepository.getSession()).thenReturn(session);

		MvcResult result = mockMvc.perform(get(SESSIONS_PATH + "{id}", "1"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map jsonResponse = responseAsMap(result);
		assertEquals(STOPPED.name(), jsonResponse.get("state"));
		assertEquals("1", jsonResponse.get("id"));
		assertEquals(1500.0, jsonResponse.get("timeLeft"));
	}

	@Test
	void runSession() throws Exception {
		HuemodoroSession session = new HuemodoroSession();
		session.run();

		when(sessionRepository.runSession()).thenReturn(session);

		MvcResult result = mockMvc.perform(put(SESSIONS_PATH + "{id}/run", "1"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map jsonResponse = responseAsMap(result);
		assertEquals(RUNNING.name(), jsonResponse.get("state"));
	}

	@Test
	void stopSession() throws Exception {
		HuemodoroSession session = new HuemodoroSession();
		session.stop();

		when(sessionRepository.stopSession()).thenReturn(session);

		MvcResult result = mockMvc.perform(put(SESSIONS_PATH + "{id}/stop", "1"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map jsonResponse = responseAsMap(result);
		assertEquals(STOPPED.name(), jsonResponse.get("state"));
	}

	@Test
	void resetSession() throws Exception {
		HuemodoroSession session = new HuemodoroSession();
		session.stop();

		when(sessionRepository.resetSession()).thenReturn(session);

		MvcResult result = mockMvc.perform(put(SESSIONS_PATH + "{id}/reset", "1"))
								  .andExpect(status().isOk())
								  .andReturn();

		Map jsonResponse = responseAsMap(result);
		assertEquals(STOPPED.name(), jsonResponse.get("state"));
	}

	private Map responseAsMap(MvcResult result) throws Exception {
		return new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
	}
}
