# Development of AlgoLink Modules

## Discovery of Modules

- ServiceLoader looking for types for organization configuration objects, writers, serializers, and application plugins.
- Searches all jar files in the CLASSPATH
- AlgoLink's command line interface can add individual jar files and directories of jar files to the CLASSPATH at runtime.
- The GUI searches ~/.ArtisTech/AlgoLink/lib automatically and all jar files found are added.

The maven project file uses the serviceloader-maven-plugin plugin to automatically generate the desired resource files for the desired classes.

```xml
<plugin>
    <groupId>eu.somatik.serviceloader-maven-plugin</groupId>
    <artifactId>serviceloader-maven-plugin</artifactId>
    <version>1.0.7</version>
    <configuration>
        <services>
            <param>com.artistech.algolink.core.OrgConfigBase</param>
            <param>com.artistech.algolink.IAlgoLinkApplicationListener</param>
            <param>com.artistech.algolink.serializers.IAlgoLinkSerializer</param>
            <param>com.artistech.algolink.listeners.ISimulationComplete</param>
            <param>com.artistech.algolink.listeners.ISimulationStart</param>
            <param>com.artistech.algolink.listeners.ITickComplete</param>
            <param>com.artistech.algolink.core.media.CommunicationMediumType</param>
            <param>com.artistech.algolink.mobility.MobilityType</param>
            <param>com.artistech.algolink.writers.IAlgoLinkWriter</param>
            <param>com.artistech.algolink.core.commfilters.ICommunicationFilter</param>
            <param>org.apache.commons.beanutils.Converter</param>
            <param>com.artistech.utils.converters.ConverterRegister</param>
        </services>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Auto-Instantiation

It should be noted that every Class that derives from one of the types above may be instantiated. For this reason, when using the ISimulationStart or ISimulationComplete interfaces to generate output for a specific organization, the Population should be searched for the desired Organization type and then have the work performed. An example of this is in the [Organizations](organizations.md) description.

The AlgoLink CLI does not auto-instantiate:

- com.artistech.algolink.core.OrgConfigBase
- com.artistech.algolink.writers.IAlgoLinkWriter
- com.artistech.algolink.serializers.IAlgoLinkSerializer
- com.artistech.algolink.core.commfilters.ICommunicationFilter

The AlgoLink GUI does auto-instantiate every type. However, the GUI is used to enable/disable individual module functionality.
