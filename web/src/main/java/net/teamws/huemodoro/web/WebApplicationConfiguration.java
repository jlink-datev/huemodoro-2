package net.teamws.huemodoro.web;

import org.springframework.boot.web.server.*;
import org.springframework.boot.web.servlet.server.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebApplicationConfiguration implements WebMvcConfigurer {

	/**
	 * Redirect all 404s to the Angular's index.html in order to allow reload of Angular pages with modified URLs
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/notFound").setViewName("forward:/index.html");
	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
		return container -> {
			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
		};
	}

	/**
	 * Allow CORS access from all places
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOrigins("*")
				.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE");
	}

}