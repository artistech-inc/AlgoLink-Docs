#Output

## Writers
Writers will stream data as the simulation is executing. Events are registered and will fire which will allow a writer to write to a file, network, database, GUI, or any other available resource.

 * Simple [JSON](writers/JsonCommsWriter.java) example.

This example will initialie a file with a starting bracket '[' and will close the file when complete with ']' to denote an array. The output file is a gzip compressed file.

## Serializers
Unlike the streaming writers, serializers should be invoked as necessary either in an init/end script or within another module.  Altough they are detected by the ServiceLoader, this is used for the GUI which will serialize upon initialization ofthe simulatoin.
 * Simple [JSON](writers/JsonSerializer.java) example.
```java
IAlgoLinkSerializer serializer = new JsonSerializer();
serializer.serialize(db, pop);
```