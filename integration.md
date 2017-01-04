---
layout: page
title: Third-Party Integration
permalink: /integration/
---

Using tomcat or Jetty, AlgoLink can be accessed as a web-service for integration into external applications.

Integration is performed using GET/POST to exposed functionality. These are ReST-like; but to aid in development.

## bindings

- [C++](/Cpp/)
- [Java](/Java/)
- [Python](/Python/)


## Shim/EMANE

Integration with [EMANE](http://www.nrl.navy.mil/itd/ncs/products/emane) is done using the shim-core from BBN.  Source is located in the NSCTA SVN repository.

An example of integrating with the EMANE can be found [here](/bsh/ent/emane-noise.bsh).  

# Important Parts  

When setting up AlgoLink to work with the SHIM, it is necessary to follow the below order of operations.  
1. Create desired organizations
2. Create the shim comm filter
3. Create the PopulationConfig
4. Get *ALL* the entities.
   - This part is important in that all entities must be instantiated in AlgoLink.
   - It is not necessary to *DO* anything with these entities other than setting the name (#6).
5. Get the list of entities from the shim filter config.
6. Set the name to each instantiated entity based on the organization and filter.
   - This name is necessary for the communication filter to know what EMANE node is associated with each AlgoLink entity.
   - Setting the name for the organization can be important if the organization needs to make use of a "structure".  Such as groupings of nodes in EMANE.  The [emane.bsh](/bsh/ent/emane.bsh) script is an example of this.

```java
//create the organization
Organization org = ec.createOrganization(Calendar.getInstance());

//create the shim config filter
//this should be done before simply to get a count of how many
//nodes are in the shim config.
ShimCommFilterConfig filter_config = new ShimCommFilterConfig();
filter_config.setStackLoc(stackLoc);

//create the shim communication filter
ShimCommFilter filter = new ShimCommFilter(filter_config);

//create the population
pc.setSize(Math.max(filter_config.getNames().size(), org.getMaxSize()));
Population pop = pc.createPopulation(Calendar.getInstance());

//this must be done here to instantiate the Entity objects inside of AlgoLink
random_entities = pop.getRandom(filter_config.getNames().size());

//take each entity and register with the shim comm filter
//registering the name w/ the organization will create any
//pairings that may be necessary (i.e. for group structure)
int count = 0;
ArrayList ents = new ArrayList(filter_config.getNames());
for(Entity ent : random_entities) {
    if(count < ents.size()) {
        ent.setName(filter, ents.get(count));
        ent.setName(org, ents.get(count));
        count += 1;
        logger.log(Level.INFO, ent.getName(filter));
    }
}

//add the organization(s) to the population
pop.addOrganization(org, Calendar.getInstance());

logger.log(Level.INFO, "Overriding any value set in ExternalCommFilterConfig.properties for timeout");
com.artistech.algolink.core.commfilters.ExternalCommFilter.setTimeout(shimTimeout);

logger.log(Level.INFO, "Overriding any value set in ShimCommFilterConfig.properties for size");
//This value should now be overriden by ec.getRequest/ResponseSize()
filter.setSize(responseSize);
//set the filter in the databuilder config
dbc.setCommFilter(filter);
```
