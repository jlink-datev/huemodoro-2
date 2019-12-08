package net.teamws.huemodoro.web;

import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

@Component
@ConfigurationProperties("hue")
public class HueConfiguration {

	private String host;
	private String client;
	private String lamp = "1";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getLamp() {
		return lamp;
	}

	public void setLamp(String lamp) {
		this.lamp = lamp;
	}
}
