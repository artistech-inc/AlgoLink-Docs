# Organizational Models

 * Simple [noise](noise) example.

## Lifetime of an Organization

1. Constructor Called with Configuration Object
2. populateHelper(Population, Calendar) called during Population.addOrganization(Organization, Calendar)
3. initialize(Calendar, com.artistech.geo.bounding.BoundingArea) called during DataBuilder.initialize(Calendar)
4. tick(Calendar) called during simulation until duration has been satisfied

## Listening for Simulation Start/Complete

Implementing the com.artistech.algolink.listeners.ISimulationStart interface will allow a class to execute a method before the simulation has started. This can be used for opening/closing resources or log files.

```java
public class NoiseSimStartListener implements ISimulationStart {

    @Override
    public void simulationStart(DataBuilder db, Population pop, Calendar time) {
        for(Organization org : pop.getOrganizations()) {
            if (NoiseOrganization.class.isAssignableFrom(org.getClass())) {
                //do work...
            }
        }
    }
    
}
```

The same is true for simulation complete.
```java
public class NoiseSimComplete implements ISimulationComplete {

    @Override
    public void simulationComplete(DataBuilder db, Population pop, Calendar time) {
        for(Organization org : pop.getOrganizations()) {
            if (NoiseOrganization.class.isAssignableFrom(org.getClass())) {
                //do work...
            }
        }
    }
}
```