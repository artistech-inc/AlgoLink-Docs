# Development of AlgoLink Modules

## Discovery of Modules
 * ServiceLoader looking for types for organization configuration objects, writers, serializers, and application plugins.
 * Searches all jar files in the CLASSPATH
 * AlgoLink's command line interface can add individual jar files and directories of jar files to the CLASSPATH at runtime.
 * The GUI searches ~/.ArtisTech/AlgoLink/lib automatically and all jar files found are added.

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