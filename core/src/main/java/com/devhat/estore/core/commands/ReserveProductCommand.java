/**
 * 
 */
package com.devhat.estore.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

/**
 * @author shyam
 * This command would be used by ProductAggregate Command so annotation it with 
 * @TargetAggregateIdentifier
 */

@Data
@Builder
public class ReserveProductCommand {
	
	@TargetAggregateIdentifier
	private final String productId;
	private final int quantity;
	private final String orderId ;
	private final String userId;

}
