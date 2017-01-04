---
layout: page
title: AlgoLink-Lib Java
permalink: /Java/
---

## Requirements

- Java >= 7
- maven

## Compile

```
mvn clean package
```

## Use

## Known Issues

If the AlgoLink server returns an int (or possibly other base types), it cannot be parted to a `JSONObject` type and will return as just an Object.

If any problems occur using this library, please e-mail [Matt Aguirre](matta@artistech.com).

# Files

- [AlgoLink.java](src/main/java/com/artistech/AlgoLink.java)
- [pom.xml](pom.xml)
