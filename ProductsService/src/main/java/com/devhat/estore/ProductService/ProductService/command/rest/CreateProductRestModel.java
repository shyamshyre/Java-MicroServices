/**
 * 
 */
package com.devhat.estore.ProductService.ProductService.command.rest;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author shyam
 *
 */

@Data
public class CreateProductRestModel {
//	Bean-Util Validation & return the messages to the client
	@NotBlank(message="Product title is a required field")
	private String title;
	@Min(value = 1, message = "Price cannot be lower thank 1$")
	private BigDecimal price;
	@Min(value = 1, message = "Quantiy cannot be lower thank 1")
	@Max(value = 5, message = "Quantiy cannot be greater thank 5")
	private Integer quantity;

}
