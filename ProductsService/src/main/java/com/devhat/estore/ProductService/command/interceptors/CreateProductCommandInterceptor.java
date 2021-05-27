/**
 * 
 */
package com.devhat.estore.ProductService.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductService.ProductService.command.CreateProductCommand;
import com.devhat.estore.ProductService.ProductService.core.data.ProductLookupEntity;
import com.devhat.estore.ProductService.ProductService.core.data.ProductLookupRepository;

/**
 * @author shyam
 *
 */

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	private final ProductLookupRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository){
		this.productLookupRepository = productLookupRepository;
	}
	
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
		// TODO Auto-generated method stub
		
		// This is repeated validation but for the sake of the demonstration we are repeating it.
		
		return (index,command) -> {
			//Identifying which interceptor it is
			LOGGER.info("Intercepted Command" + command.getPayloadType());
			
			if (CreateProductCommand.class.equals(command.getPayloadType())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();

//				WE want to perform a check whether the specific product specified does exist
//				in the database or not so we will query the lookup database /table.
//				Remember we are using a seperate table rather than a database
				
			ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(),createProductCommand.getTitle());
				if(productLookupEntity !=null) {
					throw new IllegalStateException(
					String.format("Product with PropductId %s or Product title %s already exists",
							createProductCommand.getProductId(),createProductCommand.getTitle())
					);
					
				}
			}
				
//				if needed we can perform  validation , if not we can remove them
//				already these validations are done at the bean level
				
//				if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <=0) {
//					throw new IllegalArgumentException("Price cannot be less than zwero");
//				}
//				if(createProductCommand.getTitle().isBlank() || createProductCommand.getTitle() == null) {
//					throw new IllegalArgumentException("Title cannot be empty");
//				}
			
			return command;
		};
	}

}

