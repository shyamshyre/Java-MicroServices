/**
 * 
 */
package com.devhat.estore.ProductsService.command;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

/**
 * @author shyam
 *This acts like holder of information.
 *This is a read-only class.
 *To make it read-only we need to add final.
 *This will not generate setter/constructors.
 *As the properties are final lombok will generate getter methods for this class.
 *Command class should have unique id field.
 *
 */

@Builder
@Data
public class CreateProductCommand {
	@TargetAggregateIdentifier
	private final String productId;
	
	//Axon framework will use this identifier to associate this comamnd with 
	// aggregate object in your application.
	
	private final String title;
	private final int quantity;
	private final BigDecimal price;

}
