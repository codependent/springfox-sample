package com.codependent.springfox;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String SLASH_API = "/api";
    private static final String API_KEY_NAME = "api_key";
            
    @Value("${api.key:dummyAPIKey}")
    private String apiKey;

    /*-----------------------------------------------------------------------*/
    /*                                 V1                                    */
    /*-----------------------------------------------------------------------*/
    
    /**
     * bean de configuración principal de Swagger
     * @return objeto docket
     */
    @Bean
    public Docket fooV1Api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/v1/.*"))                
            .build()
            .apiInfo(new ApiInfo("Foo Rest API","Foo REST API","v1","","","",""))
            .pathMapping(SLASH_API)
            .securitySchemes(newArrayList(apiKey()))
            .securityContexts(newArrayList(securityContext()))
            .useDefaultResponseMessages(false)
        	.groupName("authV1");
    }
    
    @Bean
    protected SecurityConfiguration securityConfiguration() {
    	return new SecurityConfiguration(
    			"test-client-id", 
    			"test-client-secret", 
    			"test-client-realm", 
    			"test-app", 
    			apiKey, 
    			ApiKeyVehicle.HEADER, 
    			API_KEY_NAME, 
    			",");
    }
    
    /**
     * Seguridad acceso al API
     * @return
     */
    private ApiKey apiKey() {
        return new ApiKey(apiKey, API_KEY_NAME, "header");
    }
    
    /**
     * Establece contexto de seguridad para el API a través de Swagger
     * @return security contextO
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/v1/.*"))
                .build();
    }
    
    /**
     * Definición contextos de seguridad para el API
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope=new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0]=authorizationScope;
        return newArrayList(new SecurityReference(apiKey, authorizationScopes));
    }
    
    private <T> List<T> newArrayList(T element){
    	List<T> l = new ArrayList<T>();
    	l.add(element);
    	return l;
    }
    
}