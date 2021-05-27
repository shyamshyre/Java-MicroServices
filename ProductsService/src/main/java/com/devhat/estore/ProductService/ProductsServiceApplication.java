package com.devhat.estore.ProductService;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import com.devhat.estore.ProductService.command.interceptors.CreateProductCommandInterceptor;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProductsServiceApplication.class);
	    app.run(args);
	}
	
//	Registering the interceptor to intercept the Command Bus.
	
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandbus) {
		commandbus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));	
	}

}
