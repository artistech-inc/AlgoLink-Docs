# Communication Filters

## Synchronous

Synchronous communication filters are communication filters that will do the filtering and decission logic in the same Java thread. There should be no communication to an external application.

- Simple [PassThrough](filters/PassThrough.java) example. Will always send.
- [Blocking](filters/BlockingFilter.java) example. Will send if the entity is not currently eganged in a communication.
- [PercentDropFilter](filters/PercentDropFilter.java) example. Will drop a specified percent of the communications randomly.

## Asynchronous

These communication filters will require a secondary process to evaluate if and when the communication starts and ends.

- [WebCommFilter](filters/WebCommFilter.java) example. The WebCommFilter can be used in conjunction with tomcat or Jetty to query for communications and tell which communications should start/end.
