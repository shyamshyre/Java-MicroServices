/**
 * 
 */
package com.devhat.estore.ProductsService.rest.model;

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
	@NotBlank(message="Product title is a required Filed")
	private String title;
	@Min(value=1, message="Price cannot be lower than 1")
	private BigDecimal price;
	
	@Min(value=1, message= "Quantity cannot be lower than 1")
	@Max(value=10, message= "Quantity cannot be larger than 10")
	private Integer quantity;
	

}
