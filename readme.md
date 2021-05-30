# Spring - MicroServices-Axon Large Scale Application Distributed Transaction System.
# Application State Management 

We will discuss about following things.

Loosely Coupled Architecture.
Domain Driven model.
Event Driven Architecture.
CQRS Design Pattern.
Application Management using CQRS.
Axon Framework.

Command Gateways.
EventBus.
Commands.
Server Side Bean Validations.
Command Interceptors.
Command Aggregates.
Command Handlers.
Dispatching Events using AggreagateLifeCycle.
EventSourceHandlers
EventHandlers.


## Application Event Flow 
## Rest Controller --> CommandGateway - > Command Bus -> Message Interceptor -> Command Handler -> EventSourceHandler-> (Query) Event Handler -> Persistence.

### Set Based Consistency --> Solving the major huddle.
### Global Exception Handling --> 




# How to Handle Distributed Transaction effectively using DomainDrivenModel ,EventDrivenArchitecture applying CQRS & SAGA Design Patterns.
when you are working on large scale applications that are distributed across multiple nodes and how effectively we attain communication across these.

What we See 

### SetBased Consistency Validation : In simple words when you decoupleyour architecture using CQRS design pattern,
#### The major problem we see is StateValidation, As the data gets persisted first on the ReadDatabase and then the 
communication happens through Eventbus which can lead to Inconsistent State, Validating the aggregated state is even 
a time consuming process.
To avoid this problem better to implement Lookup Commmand Database at the command side which helps in effective lookup Reads.
This really solves the major problem of SetBasedConsistency Validaton.

Lookup doesnt need to contain the complete record set information which exists on Query/Read Database.
You can have properties like transaction id, orderid, product id etc.. To validate the Record.
Storing the complete information leads to redundancy.




Command Handler - Post Requests

POST: http://localhost:9090/products-service/products
BODY - APPLICATION/JSON
{
    "title":"tesla-rati",
    "price":900,
    "quantity":1
}

QueryHandler - GET Requests
http://localhost:9090/products-service/products
Output:
[
    {
        "productId": "98496bbb-64ec-4f5c-a1b5-f86499be5160",
        "title": "janga2",
        "quantity": 4,
        "price": 900.00
    },
    {
        "productId": "eefc7653-5606-453a-89f7-c10df0151e92",
        "title": "janga1",
        "quantity": 4,
        "price": 900.00
    }
]


### Saga LifeCycle.
@StartSaga Begining of the Saga LifeCycle.
association property could be any property from the event which you are handling.
Makesure that the event holds the property while it being dispatched earlier and before being handled at Saga.
@SagaEventHandler(associationProperty="")
public void handle1(Event evt)
{
All the methods inside the Saga Event handler capture the events and then dispatch Commands to the next lifecycle of transaction.
}

@SagaEventHandler(associationProperty="")
public void handle2(Event evt)
{
All the methods inside the Saga Event handler capture the events and then dispatch Commands to the next lifecycle of transaction.
}

@EndSage - Sagalifecycle Ends.
@SagaEventHandler(associationProperty="")
public void handle2(Event evt)
{
All the methods inside the Saga Event handler capture the events and then dispatch Commands to the next lifecycle of transaction.
}



