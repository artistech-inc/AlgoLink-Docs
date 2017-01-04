---
layout: page
title: Entity Reaction Behavior
permalink: /development/entity-reaction/
---

`Entities` are able to react as well as simply be. `Entity` reaction is implemented on an organizational level, so what an entity does in Organization A has no bearing on what the entity will do in Organization B.

- [Consensus2Entity](../Consensus2Entity.java): This example shows how an entity may react upon receiving a communication.

Not shown is the ability to then create and `inject` a communication back into the simulation. This is often used as a response mechanism.

```java
Communication c = new Communication(getEntity(), receiverEntity, start);
c.setCommunicationMessage(m);
c.setEnd(end);
getEntity().inject(c);
```

The `DataBuilder` object registers as an injection listener to all entities, so if an injection is performed, the communication will be inserted and handled appropriately.

## Events

```java
@Override
public void onCommunicationSending(Entity sender, Communication comm, CommunicationTransactionEventArgs args) {
}

@Override
public void onCommunicationReceiving(Entity receiver, Communication comm, CommunicationTransactionEventArgs args) {
}

@Override
public void onCommunicationSent(Entity sender, Communication comm) {
}

@Override
public void onCommunicationReceived(Entity receiver, Communication comm) {
}

@Override
public void onMessageReceived(Entity sender, String message, Calendar time, HashMap<String, Object> values) {
}
```

Sending and Receiving events have the ability to cancel a communication if it is necessary to do so.<br>
Sent and Received events are used to show that the transmission of the communication is complete.<br>
The `onMessageReceived()` event is used for sending messages to an entity via an HTTP post using tomcat or Jetty. Or some other mechanism where a message is being sent from an external source.
