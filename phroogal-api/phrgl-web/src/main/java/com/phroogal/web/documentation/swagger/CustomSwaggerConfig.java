/**
 * 
 */
package com.phroogal.web.documentation.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.core.SwaggerApiResourceListing;
import com.mangofactory.swagger.ordering.ResourceListingPositionalOrdering;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.mangofactory.swagger.scanners.ApiListingReferenceScanner;
import com.wordnik.swagger.model.ApiInfo;

/**
 * Configures swagger api documentation
 * 
 * @author Christopher Mariano
 * 
 */
@Configuration
@EnableSwagger
@ComponentScan(basePackages = "com.mangofactory.swagger")
public class CustomSwaggerConfig {
	
	public static final List<String> DEFAULT_INCLUDE_PATTERNS = Arrays.asList(
			"/api/login/.*"
	);
	
	public static final String SWAGGER_GROUP = "phroogal-api";

	@Autowired
	private SpringSwaggerConfig springSwaggerConfig;


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
		swagger.build();
		return swagger;
	}
	
	@Bean
    public SwaggerApiResourceListing swaggerApiResourceListing() {
        SwaggerApiResourceListing swaggerApiResourceListing = new SwaggerApiResourceListing(springSwaggerConfig.swaggerCache(), SWAGGER_GROUP);
        swaggerApiResourceListing.setApiListingReferenceScanner(apiListingReferenceScanner());
        return swaggerApiResourceListing;
    }
	
    /**
     * The ApiListingReferenceScanner does most of the work.
     * Scans the appropriate spring RequestMappingHandlerMappings
     * Applies the correct absolute paths to the generated swagger resources
     */
	@Bean
    public ApiListingReferenceScanner apiListingReferenceScanner() {
        ApiListingReferenceScanner apiListingReferenceScanner = new ApiListingReferenceScanner();
        apiListingReferenceScanner.setRequestMappingHandlerMapping(springSwaggerConfig.swaggerRequestMappingHandlerMappings());
        apiListingReferenceScanner.setExcludeAnnotations(springSwaggerConfig.defaultExcludeAnnotations());
        apiListingReferenceScanner.setResourceGroupingStrategy(springSwaggerConfig.defaultResourceGroupingStrategy());
        apiListingReferenceScanner.setIncludePatterns(DEFAULT_INCLUDE_PATTERNS);

        return apiListingReferenceScanner;
    }

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Phroogal Developer API", "Phroogal API documentation", null,
				null, null, null);
		return apiInfo;
	}
}
