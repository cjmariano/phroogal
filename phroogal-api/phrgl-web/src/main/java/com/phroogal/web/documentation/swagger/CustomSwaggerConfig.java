/**
 * 
 */
package com.phroogal.web.documentation.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.ordering.ResourceListingPositionalOrdering;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

/**
 * Configures swagger api documentation
 * 
 * @author Christopher Mariano
 * 
 */
@Configuration
@EnableSwagger
public class CustomSwaggerConfig {

	private SpringSwaggerConfig springSwaggerConfig;

	/**
	 * Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		SwaggerSpringMvcPlugin swagger = new SwaggerSpringMvcPlugin(
				this.springSwaggerConfig).apiInfo(apiInfo());
		swagger.apiListingReferenceOrdering(new ResourceListingPositionalOrdering());
		return swagger;
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Phroogal Developer API", "Phroogal API documentation", null,
				null, null, null);
		return apiInfo;
	}
}
