package com.devhat.estore.ProductsService;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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
}