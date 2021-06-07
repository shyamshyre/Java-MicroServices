package com.devhat.estore.core.commands;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductReservedEvent {
	private final String productId;
	private final int quantity;
	private final String orderId ;
	private final String userId;
}
