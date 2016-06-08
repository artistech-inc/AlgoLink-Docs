# Event Listeners

Implementing event listeners is how AlgoLink can be quickly extended to add new functionality.

## IEntityListener

This listener is implemented by the EntityWrapperBase class. However, if additional listeners are desired, then this can be added to the entity class to catch and handle events.

## ClockListener

AlgoLink is driven by a discrete 'clock' that will run and process events in serial. The clock can be accessed via a static method in the DataBuilder class:

```java
public static final IClock getClock(Population pop);
```

Where the Population object can be caught during the populateHelper() method in an Organization.

It is not necessary to implement the ClockListener, but rather it is easier to simply insert new events into the IClock instance that is returned.

## DataBuilder Listeners

[ISimulationStart](listeners/ISimulationStart.java), [ISimulationComplete](listeners/ISimulationComplete.java), [ITickComplete](listeners/ITickComplete.java), together make [IDataBuilderListener](listeners/IDataBuilderListener.java). However, as a design decision, the IDataBuilderListener does NOT extend the other 3 interfaces because then the ServiceLoader will load IDataBuilderListener derived classes multiple times. These interfaces are useful for generating/serializing organization specific output.

Implementations of these classes are automatically instantiated and loaded into the the DataBuider.

## Communication Listening

 - [IOnCommunicationListener](listeners/IOnCommunicationListener.java) is used for listening to the DataBuilder for when a communication begins to send and ends.
 - [ICommunicationListener](listeners/ICommunicationListener.java) is used for registering directly with Communication objects for events. It is possible to chain these by adding an IOnCommunicationListener to the DataBuilder, then in the onCommunication() event add an ICommunicationListener to the Communication object(s).

## Population Listening

Find out when population level events occur such as clearing.

## Group Listening

Useful for monitoring sub-groups within an organization.

## Application Listening

[IAlgoLinkApplicationListener](listeners/IAlgoLinkApplicationListener.java) derived classes are instantiated and invoked when the application starts and ends cleanly.  For example, is how the Jetty server is started/stopped in the enterprise module.
