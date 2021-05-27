package com.devhat.estore.ProductService;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import com.devhat.estore.ProductService.command.interceptors.CreateProductCommandInterceptor;
import com.devhat.estore.ProductService.core.errorhandling.ProductServiceEventErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProductsServiceApplication.class);
	    app.run(args);
	}
	
//	Registering the intercepter to intercept the Command Bus.
	
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandbus) {
		commandbus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));	
	}
	
	// We are creating a EventProcessingConfigurer object and this will be executed at startup .
	// We will use this object to register the ListenerInvocationErrorHandler for a specific processing group.
	
	@Autowired
    public void configure(EventProcessingConfigurer configurer) {
//		This is custom invocation error handler.
		configurer.registerListenerInvocationErrorHandler("product-group", conf-> new ProductServiceEventErrorHandler());
//      if custom is not required then we can go with dfault Axon Framework handler 		
//		configurer.registerListenerInvocationErrorHandler("product-group", conf-> new PropogatingErrorHandler.instance());
    }

}
