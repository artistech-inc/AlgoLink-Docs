---
layout: page
title: Logging
permalink: /logging/
---
AlgoLink uses the native `java.util.logging.Logger` classes and is configured using the `~/.ArtisTech/AlgoLink/logging.properties` file

Example:

```
.level = SEVERE
sun.level = SEVERE
javax.level = SEVERE
java.level = SEVERE
org.level = SEVERE
com.level = SEVERE

com.artistech.algolink.level = FINER
com.artistech.level = FINER
com.artistech.algolink.core.DataBuilder.level = FINE
com.artistech.algolink.core.DataBuilder$Processor.level = INFO

# Logging
handlers = java.util.logging.ConsoleHandler, java.util.logging.FileHandler

# File Logging
java.util.logging.FileHandler.pattern = %h/.ArtisTech/AlgoLink/log/algolink-%u-%g.log
java.util.logging.FileHandler.formatter = com.artistech.utils.logging.ShortenedNameFormatter
java.util.logging.FileHandler.level = FINER

# Console Logging
java.util.logging.ConsoleHandler.formatter = com.artistech.utils.logging.ShortenedNameFormatter
java.util.logging.ConsoleHandler.level = FINE

com.artistech.utils.logging.ShortenedNameFormatter.format = %1$tFT%1$tT %4$s [%3$s] %5$s %6$s%n
```

Anytime that AlgoLink runs and the `logging.properties` file is not present, it will recreate the file with the default AlgoLink logging properties file.

## Overloading the file output on the command line

The `java.util.logging.FileHandler.formatter` can be specified directly from the command line which will override any value that is specified in the `logging.properties` file.

```
java -Dalgolink.logfile=/tmp/algolink.log -jar algolink-10.12.jar ....
```

## Formatters

The custom formatter `com.artistech.utils.logging.ShortenedNameFormatter` will print out the first letter of the package path.

```
2016-06-07T14:33:00 FINE [caul.SwingComponentHandler] Started...
```

SwingComponentHandler is located in com.artistech.utils.logging.

## GUI Console logging

The custom handler for printing to a Java Swing GUI component is `com.artistech.utils.logging.SwingComponentHandler` and can be added to the handlers section if desired. If the container class for the GUI cannot be found (AlgoLink not running in GUI mode) then the handler is removed from the list.

```
# Logging
handlers = com.artistech.utils.logging.SwingComponentHandler

#GUI Logging
com.artistech.utils.logging.SwingComponentHandler.formatter = com.artistech.utils.logging.ConsoleLogFormatter
com.artistech.utils.logging.SwingComponentHandler.level = FINE
#this is the bolded portion of the log record:
com.artistech.utils.logging.SwingComponentHandler.format = %4$s [%3$s]
#set the colors of the log record for each level:
#Hex and 'known colors' are accepted as values for all color fields
com.artistech.utils.logging.SwingComponentHandler.warning = 0xFF6600
com.artistech.utils.logging.SwingComponentHandler.severe = 0x660000
com.artistech.utils.logging.SwingComponentHandler.info = 0x305020
#com.artistech.utils.logging.SwingComponentHandler.default = 0x000000
com.artistech.utils.logging.SwingComponentHandler.default = BLACK

com.artistech.utils.logging.ConsoleLogFormatter.format = %5$s %6$s%n
```
