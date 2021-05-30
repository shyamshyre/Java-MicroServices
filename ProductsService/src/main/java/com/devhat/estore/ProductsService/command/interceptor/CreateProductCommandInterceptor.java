/**
 * 
 */
package com.devhat.estore.ProductsService.command.interceptor;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductsService.command.CreateProductCommand;
import com.devhat.estore.ProductsService.entity.ProductLookupEntity;
import com.devhat.estore.ProductsService.repository.ProductLookupRepository;

/**
 * @author shyam
 * This is an interceptor which can intercept the messages before reachign out to CommandHandler
 *
 */

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	private static final Logger logger = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	
	private final ProductLookupRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
		
	}
	
	
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		logger.info("CreateProductCommandInterceptor Begining");
		// TODO Auto-generated method stub
		return (index,command) -> {
//			This will intercept any other Commands such as UpdateProductCommand
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
				logger.info("Intercepted"+ command.getPayload());
//				<!-- REDUNDANT LOGIC BEAN VALIDATION -->
//				if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <=0) {
//					throw new IllegalArgumentException("Price cannot be less than zero");
//				}
//				if(createProductCommand.getTitle().isBlank()) {
//					throw new IllegalArgumentException("Title cannot Empty");
//				}
			ProductLookupEntity productLookupEntity =	productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());
			if(productLookupEntity !=null) {
				throw new IllegalStateException(
				String.format("Product with productid %s or Title %s already exist",
						createProductCommand.getProductId() ,createProductCommand.getTitle())
				);
			}
			}
			logger.info("CreateProductCommandInterceptor Ending");	
			return command;
		};
	}

}
