/**
 * 
 */
package com.devhat.estore.ProductsService.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devhat.estore.ProductsService.command.CreateProductCommand;
import com.devhat.estore.ProductsService.rest.model.CreateProductRestModel;

/**
 * @author shyam
 *
 */

@RestController
@RequestMapping("/products")  //http://localhost:8080/products 
public class ProductsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);
    private final Environment env;
	private final CommandGateway commandGateway;
	
	@Autowired
	public ProductsController(Environment env ,CommandGateway commandGateway ) {
		this.env=env;
		this.commandGateway = commandGateway;
	}
	
	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
		
		CreateProductCommand createProductCommand = CreateProductCommand.builder().price(createProductRestModel.getPrice())
		.quantity(createProductRestModel.getQuantity())
		.title(createProductRestModel.getTitle())
		.price(createProductRestModel.getPrice())
		.productId(UUID.randomUUID().toString()).build();
		//Return value in this is productID
		String returnValue = null;
		try {	
		  returnValue= commandGateway.sendAndWait(createProductCommand);
			// send will just send and doesnt block the execution.
		 	// sendAandWait will block immediately until the immediate result is available.
			// return "HTTP POST METHOD"+ "Running on SERVER PORT"+env.getProperty("local.server.port") ;
			// return " HTTP POST METHOD" + "Title"+createProductRestModel.getTitle();

		}catch(Exception ex) {
			LOGGER.info(ex.getMessage());	
		}
		return returnValue;
	}

}
