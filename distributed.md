# AlgoLink Enterprise

Version 10.12

## Requirements

- Java >= 7
- OpenMQ >= 5

## Installation

Extract the AlgoLink-Ent.tar.gz file Install OpenMQ somewhere that is accessible via a network connection.

## Test Installation

A quick test to run would be to simply invoke the main method of the jar file w/o any arguments.

```
java -jar algolink-ent-10.12.jar
```

This will then start up a Jetty web server on port 8081 (if this fails, it will keep incrementing ports for 10 values until it finds a port that is available). A message is printed out about loading bean shell scripts. And then the application will close down the Jetty web server and exit.

## Design Considerations

The master and each satellite is designed to run on a separate computer/VM. The application uses properties files in a hidden directory to determine execution. These properties files are located in: `~/.ArtisTech/AlgoLink/conf`

## Configuration

- Simulation Run-Time Configuration of Satellite Organizations: The master is configured with all of the desired organizations and then tells individual satellites how to be configured.
- Pre-configured Satellite Organizations: The satellites are configured with one or more organizations that will be used in the simulation no mater how the master is configured.

### Simulation Run-Time Configuration of Satellite Organizations

This is the default mode for AlgoLink. Each organization is parceled out to waiting satellites.

#### Master

You will need to configure this file: `conf-examples/distributed/master/OpenMqFactory.properties` and set the IP address of the installed OpenMQ server.

These file(s) will need to be located in `~/.ArtisTech/AlgoLink/conf/`

--------------------------------------------------------------------------------

#### Satellites

```
java -jar algolink-ent-10.12.jar -bsh node.bsh
```

You will need to configure two files:

- `Random.properties` set the seed value to a unique value amongst all satellites. seed = 52
- `OpenMqFactory.properties` Set the IP address of the installed OpenMQ server.

These file(s) will need to be located in `~/.ArtisTech/AlgoLink/conf/`

### Pre-configured Satellite Organizations

This is a legacy mode for AlgoLink. The new method of configuring satellites is to parcel the organizations to any listening satellites.

#### Master

You will need to configure this file: `conf-examples/distributed/master/OpenMqFactory.properties` and set the IP address of the installed OpenMQ server.

`DataBuilderConfig.properties` must set `parcelToSatellites` to false. Or the `DataBuilderConfig` value in the bean shell script value must set `setParcelToSatellites(false)`. The Default value is `true`.

These file(s) will need to be located in `~/.ArtisTech/AlgoLink/conf/`

--------------------------------------------------------------------------------

#### Satellites

satellite (sat-01): You will need to configure three files:

- `DistributedNode.properties` example confiuration:

```
name = Node1
organizations = { "scenario1" : "com.artistech.algolink.orgs.scenario1.Scenario1Config" }
scenario1 = {}

The name value is just a string, nothing is currently done with this value.
The organizations value is a JSON value where the key is a unique name and the
value is the type of organization to instantiate. In this case I am creating a
"Social Patterns" organization. The name can then be used to load further
customization values to the scenario configuration. In this case, I am accepting
the defaults.
```

- `Random.properties` set the seed value to a unique value amongst all satellites. seed = 52
- `OpenMqFactory.properties` Set the IP address of the installed OpenMQ server.

These file(s) will need to be located in `~/.ArtisTech/AlgoLink/conf/`

--------------------------------------------------------------------------------

satellite (sat-02 & sat-03): You will need to configure three files:

- `DistributedNode.properties` example confiuration:

```
name = Node1
organizations = { "noise1" : "com.artistech.algolink.orgs.noise.NoiseConfig" }
noise1 = {"commsPerMinute":500.0}

The name value is just a string, nothing is currently done with this value. The
organizations value is a JSON value where the key is a unique name and the value
is the type of organization to instantiate. In this case I am creating a Noise
organization. The name can then be used to load further customization values to
the scenario configuration. In this case, I am setting the # of communications
to 500 / minute.
```

- `Random.properties` set the seed value to a unique value amongst all satellites. seed = 52
- `OpenMqFactory.properties` Set the IP address of the installed OpenMQ server.

These file(s) will need to be located in `~/.ArtisTech/AlgoLink/conf/`

## Running

The satellite instances will need to be started first. Once each instance is configured as desired, it should be invoked using the `node.bsh` script. The `node.bsh` script will need to be extracted from the `lib/algolink-core-10.12.jar` file. It is located at `com/artistech/algolink/bsh/node.bsh`. The `master.bsh` script is located in the same directory.

Start the script in AlgoLink with the following command:

```
java -jar AlgoLink-Ent/AlgoLink-Ent.jar -bsh node.bsh
```

Once all satellite instances have started, the master can be started:

```
java -jar AlgoLink-Ent/AlgoLink-Ent.jar -bsh master.bsh
```

This will broadcast on OpenMQ for all listening satellites. After accepting that all have been found and have responded (waiting 10 seconds), the master will tell all satellites to configure themselves and then the simulation will start.

Output to the simulation is located on the master VM under `~/.ArtisTech/Algolink/commuications`
