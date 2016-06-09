# Thrid-Party Integration

Using tomcat or Jetty, AlgoLink can be accessed as a web-service for integration into external applications.

Integration is performed using GET/POST to exposed functionality. These are ReST-like; but to aid in development, bindings for [Python](Python/), [C++](Cpp/), and [Java](Java/) are provided.

## Shim

Integration with [EMANE](http://www.nrl.navy.mil/itd/ncs/products/emane) is done using the shim-core from BBN.  Source is located in the NSCTA SVN repository.
