# Development of AlgoLink Modules

## Organizational Models

 * Simple [noise](noise) example.

## Discovery of Modules
 * [serviceloader-maven-plugin](https://github.com/francisdb/serviceloader-maven-plugin)
 * ServiceLoader looking for types derived from: com.artistech.algolink.core.OrgConfigBase
 * Searches all jar files in the CLASSPATH
 * AlgoLink's command line interface can add individual jar files and directories of jar files to the CLASSPATH at runtime.
 * The GUI searches ~/.ArtisTech/AlgoLink/lib automatically and all jar files found are added.