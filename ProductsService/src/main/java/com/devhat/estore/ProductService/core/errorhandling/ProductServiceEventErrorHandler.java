/**
 * 
 */
package com.devhat.estore.ProductService.core.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

/**
 * @author shyam
 *This class by implementing the ListenerInvocation Interface will allow us to handle errors in the Eventhandlers.
 * Here we can handle or re-throw the exception which is the last level 
 * From here either we can handle the exception within a custom handler or use Axon Framework handler(PropogationException handler).
 */
public class ProductServiceEventErrorHandler implements ListenerInvocationErrorHandler {

	@Override
	public void onError(Exception exception, EventMessage<?> event, EventMessageHandler eventHandler) throws Exception {
		// TODO Auto-generated method stub
		throw exception;

	}

}
