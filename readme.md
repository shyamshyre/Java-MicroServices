
Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@shyamshyre 
shyamshyre
/
Java-MicroServices
1
11
Code
Issues
Pull requests
Discussions
Actions
Projects
Wiki
Security
Insights
Settings
Java-MicroServices/intro
@shyamshyre
shyamshyre Create intro
Latest commit 1744da9 3 days ago
 History
 1 contributor
129 lines (101 sloc)  4.74 KB
  
# Spring - MicroServices-Axon Large Scale Application Distributed Transaction System.
The biggest challenge in the database world is maintaining ATOMICITY luckily thanks to database which has come up in solving the problem.
But does this really solve the current today's problem where the data generation is humognous with limtiless adaption capability 
of commodity hardware that turned to be chepaer. During the good old days Vertical scaling was the biggest feast, Still it has it own
set of cons as the applicaions turned to commodites specific rather than application specific.
I can say horizontal scaling is what created distruption and this will be the nearby future, but still it comes with own set of tradeoffs.

On the brighter side.
* Significant Cost Cutting.
* Highly Scalable.
* Resilience is the Key.
* Rather than hardware specific , now solutions are application specific.

On the lighter side.
 * Challenges in managing multiple systems.
 * Special Expertise.
 * Managing ATOMICITY turns to be huge challenge.
 
 Following are the few things which i would be discussing in this chapter.

* Application State Management 
* Loosely Coupled Architecture.
* Domain Driven model.
* Event Driven Architecture.
* CQRS Design Pattern.
* Application Management using CQRS.
* Axon Framework.

* Command Gateways.
* EventBus.
* Commands.
* Server Side Bean Validations.
* Command Interceptors.
* Command Aggregates.
* Command Handlers.
* Dispatching Events using AggreagateLifeCycle.
* EventSourceHandlers
* EventHandlers.


## Application Event Flow 
## Rest Controller --> CommandGateway - > Command Bus -> Message Interceptor -> Command Handler -> EventSourceHandler-> (Query) Event Handler -> Persistence.

### Set Based Consistency --> Solving the major huddle.
### Global Exception Handling --> 




How to Handle Distributed Transaction effectively using DomainDrivenModel ,EventDrivenArchitecture applying CQRS & SAGA Design Patterns.
when you are working on large scale applications that are distributed across multiple nodes and how effectively we attain communication across these.

What we See 

SetBased Consistency Validation : In simple words when you decoupleyour architecture using CQRS design pattern,
The major problem we see is StateValidation, As the data gets persisted first on the ReadDatabase and then the 
communication happens through Eventbus which can lead to Inconsistent State, Validating the aggregated state is even 
a time consuming process.
To avoid this problem better to implement Lookup Commmand Database at the command side which helps in effective lookup 
Reads.

This really solves the major problem of SetBasedConsistency Validaton.

Lookup doesnt need to contain the complete record set information which exists on Query/Read Database.
You can have properties like transaction id, orderid, product id etc.. To validate the Record.
Storing the complete information leads to redundancy.

* Command Handler - Post Requests

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

* @StartSaga Begining of the Saga LifeCycle.
*association Property the Key.
Saga manages seperation between multipleservices and it can handle multiple events , we need to tell axon frameowrk which instance of saga to use
when an event is published, Axon framework will used the association property to find the neeed instance fo saga and let it handle the publisher.
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

Association can be done programtically  as well.
1) associateWith(key,value);
2) removeAssocationWith(key,Value)


© 2021 GitHub, Inc.
Terms
Privacy
Security
Status
Docs
Contact GitHub
Pricing
API
Training
Blog
About
Loading complete
