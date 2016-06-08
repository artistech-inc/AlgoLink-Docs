# AlgoLink Web Documentation

## Requirements

- Tomcat >= 7
- Java >= 7

## Installation

Put the AlgoLink-Web.war file in the appropriate deployment directory for Tomcat. Typically this is /var/lib/tomcat7/webapps. Goto http://

<web-server>:<port>/AlgoLink-Web/</port></web-server>

## Description

As well as being a web-interface for creating and running simulations, this version of AlgoLink also can be configured using external web-clients. Currently this is done using simple web-based GET/POST methods. A Python library has been implemented to aid in generating scripts. This library and this web-based approach is currently in use at ARL for the QoI integration.
