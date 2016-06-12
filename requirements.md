# Requirements

## Windows

- Desktop: [Java >= 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- Server: [Tomcat >= 7](http://tomcat.apache.org/download-70.cgi); Tomcat requires the installation of the Java JDK (>= 7)

## Linux Desktop

Developed/Tested under [Ubuntu 14.04 and 16.04](http://www.ubuntu.com/download/desktop)

- Easy install: sudo apt-get install openjdk-7-jre
- Alternately, [Oracle JDK 7](http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html)([or 8](http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html")) can be used.

### Switching between Java versions

```
sudo update-alternatives --config java

sudo update-java-alternatives -s java-1.7.0-openjdk-amd64
```

## Running

Once downloaded and extracted, AlgoLink can be run with the following command:

```
java -jar algolink-gui-10.12.jar
```

The jar file contains the CLASSPATH to reference all other necessary libraries in the lib sub-directory.

### Debian Package

Also provided for download is an experimental deb package. This will place AlgoLink in `/usr/share/algolink` and also place an algolink.desktop file in `/usr/share/applications` so that the application can be launched from Unity.

- To install the deb package:

```
sudo dpkg -i algolink-gui_latest.deb
```

- If java is not installed, this may show a failure to install. Java may be installed in the following manner: sudo apt-get install -f which will fix broken installations.

## Linux Server

Linux Server: Developed/Tested under [Ubuntu 14.04, and 16.04](http://www.ubuntu.com/download/server)

- Easy install: sudo apt-get install openjdk-7-jdk tomcat7
- Alternately, [Oracle JDK 7](http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html)([or 8](http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html")) can be used.
- Switching between Java versions: change the /usr/lib/jvm/default-java link for changing Tomcat's default JDK
- Updating the max heap of Tomcat
    - If using Ubuntu and installed Tomcat from the repos, edit the file /etc/default/tomcat7 (or tomcat8). Change the line for JAVA_OPTS (probably looks like this: JAVA_OPTS="-Djava.awt.headless=true -Xmx128m -XX:+UseConcMarkSweepGC"
    - Change the -Xmx128m to something more suitable; if possible -Xmx4g
    - Reboot; restarting Tomcat may not be enough
