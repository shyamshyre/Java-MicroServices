package com.devhat.estore.ProductService.ProductService.command.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devhat.estore.ProductService.ProductService.command.CreateProductCommand;


@RestController
@RequestMapping("/products")
public class ProductCommandController {
	Logger logger = LoggerFactory.getLogger(ProductCommandController.class);
//Injecting the environment variables using property based injection.	
//	Adding to get the port number on which the server is running.
//	@Autowired
//	private Environment env;
	
// Lets now inject using constructor based injection
	
	private Environment env;
	private CommandGateway commandGateway;
	
	@Autowired
	public ProductCommandController(Environment env , CommandGateway commandGateway) {
		this.env = env;
		this.commandGateway = commandGateway;
	}
	
	@PostMapping
	//@Valid will trigger the Validations defined in the Create Product Rest Model
	public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
		String returnValue =null;
		CreateProductCommand createProductCommand= CreateProductCommand.builder().price(createProductRestModel.getPrice())
		.quantity(createProductRestModel.getQuantity())
		.title(createProductRestModel.getTitle())
		.productId(UUID.randomUUID().toString()).build();
		try {
		 returnValue = commandGateway.sendAndWait(createProductCommand);
		 logger.info(returnValue);
		
		}catch(Exception ex) {
			returnValue = ex.getLocalizedMessage();
		}
		return returnValue;  
	}
	
//	@GetMapping
//	public String getProduct() {
////		if we print server.port then prints 0 configured in properties file.
////		But in runtime port is allocated to get it we are using local.server.port
//		return "HTTP GET Handled" + env.getProperty("local.server.port");
//	}
//	
//	@PutMapping
//	public String updateProduct() {
//		return "HTTP PUT Handeled";
//	}
//	
//	@DeleteMapping
//	public String deleteProduct() {
//		return "HTTP DELTE handled";
//	}
}
