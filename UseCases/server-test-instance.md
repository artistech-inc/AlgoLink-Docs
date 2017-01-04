---
layout: page
title: Use Cases
permalink: /UseCases/server-test-instance
---

# To run a test instance of the server

- To install: deploy latest AlgoLink war file in the Tomcat webapps directory.
    - Windows: `C:\Program Files\Apache Software Foundation\Tomcat 7\webapps`
    - Linux: `/var/lib/tomcat7/webapps/`
- Open a browser to `http://<address_of_tomcat_server>:8080/algolink-web-10.12/build.jsp`
- Under `Data Builders` Select `DataBuilder Config`
- Select `Organizations`
- Select `Social Patterns`
- Click `Add Configuration`
- Under `Action`, select `Build Population and Start Simulation`
- Click `Execute`
