# Converters

Generally, conversions in AlgoLink are for to-from String. The conversions are used for reading in String values from a properties file and converting it to the expected type.<br>
The [BeansUtil](https://commons.apache.org/proper/commons-beanutils/) package is used to facilitate this functionality. AlgoLink uses a custom wrapper around this functionality to allow different pairings of types to exist without having to know all conversions involving String in advance.

- [Color Converter](converters/ColorConverter.java) example. This will convert a Color object to a String representation and back.
- [Class Converter](converters/ClassConverter.java) example. This will convert a String name for a type and return back a Class object representing the type.

## JSON Converters

If utilizing JSON, then the individual properties for the Class being serialized must specify how to serialize. This is only needed if the default behavior is not desired. For JSON serialization and deserialization, the [Jackson JSON Processor](http://wiki.fasterxml.com/JacksonHome) is used.
