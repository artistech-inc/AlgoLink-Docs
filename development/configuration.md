# Configuring objects

Many classes in AlgoLink inherit from PropertiesInitializer. This class will look for a properties file with the same name in the CLASSPATH and use introspection/reflection to match names in the properties file to Java Bean properties in the class. Aside from also searching the CLASSPATH, the ~/.ArtisTech/AlgoLink/conf/

<classname>.properties file is also located.  If it exists, then the file is read and values placed into the object instance.  Values placed in the '/conf' directory have priority over default values found in the CLASSPATH.</classname>

As well as using these properties files for setting default values, values for many types can be set using the bsh scripts. In addition to this, values can be passed into the bsh scripts from the command line.

```
java -jar algolink-cli-10.12.jar -bsh consensus2.bsh -v k=1.1 -v q=2
```

The Bean Shell Script is set with the -bsh option, and all variables that need to be passed into the bsh script are set using the -v argument. This can allow shell scripts to invoke many different simulations serially with different parameter values.

## Types of Note

- com.artistech.algolink.writers.WriterFactory: Changing the properties here allows for setting the defaultFactory value. Default is com.artistech.algolink.writers.ThreadedWriterFactory.
- com.artistech.clock.ClockConfig: Changing the properties here allows for changing the granularity of the clock. Default is 5ms.

## At the Enterprise Level

- com.artistech.mq.ConnectionInfo: Changing the properties here allows for setting the defaultFactory value. Default is com.artistech.mq.OpenMqFactory.
- com.artistech.mq.OpenMqFactory: Set where the OpenMQ server is located. Can also specify location of the imq jar file.
- com.artistech.mq.ActiveMqFactory: Set where the ActiveMq server is located. Can also specify the location of the activemq-all jar file.
