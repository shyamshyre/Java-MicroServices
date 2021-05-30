package com.devhat.estore.ProductsService;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.devhat.estore.ProductsService.command.errorhandling.ProductServiceEventErrorHandler;
import com.devhat.estore.ProductsService.command.interceptor.CreateProductCommandInterceptor;

@SpringBootApplication
public class ProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsApplication.class, args);
	}
		@Autowired
		public void registerCreateCommandInterceptor(ApplicationContext context,
				CommandBus commandBus) {
			commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
}
		
		@Autowired
		public void configure(EventProcessingConfigurer configurer) {
			
			//This is custom error handler, we are defining it to the product-group event handlers.
			configurer.registerListenerInvocationErrorHandler("product-group" ,
					conf -> new ProductServiceEventErrorHandler());
			
//			This is axon default error handler
//			configurer.registerListenerInvocationErrorHandler("product-group" ,
//					conf -> new PropagatingErrorHandler.instance());
		}
}