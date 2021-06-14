package com.devhat.estore.userservice;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.devhat.estore.core.model.PaymentDetails;
import com.devhat.estore.core.model.User;
import com.devhat.estore.core.query.FetchUserPaymentDetailsQuery;

@Component
public class UserEventsHandler {
	
	@QueryHandler
	public User findUserPaymentDetails(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery  ) {
		PaymentDetails paymentDetails = PaymentDetails.builder()
				.cardNumber("123CARd")
				.cvv("123")
				.name("Sham")
				.validUntilMonth(04)
				.validUntilYear(2025)
				.build();
		
		
		
		User user = User.builder()
					.firstName("Shyam")
					.lastName("S")
					.uerId(fetchUserPaymentDetailsQuery.getUserId())
					.build();
		
		return user;
					
	}
	
	

}
