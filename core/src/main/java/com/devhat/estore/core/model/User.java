package com.devhat.estore.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	private final String firstName;
	private final String lastName;
	private final String uerId;
	private final PaymentDetails paymentDetails;
}
