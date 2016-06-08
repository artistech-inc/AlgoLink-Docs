# Bean Shell Scripts

Scripts are useful for maintaining easily configurable and powerful command line level processing. These scripts can also be loaded, edited, and run directly from within the AlgoLink GUI.

```
java -jar algolink-cli-10.12.jar -bsh consensus2.bsh -v k=1.1 -v q=2
```

The script to run is set with the -bsh option, and variables that need to be passed into the bsh script are set using the -v argument. This can allow shell scripts to invoke many different simulations serially with different parameter values.

When writing scripts; detect passed in variables in the script:

```java
if (k == void) {
    //set a default value if no value is passed in.
    k = 4;
} else {
    //read in the passed in value; the passed in value is of type String and may require conversion.
    k = Double.parseDouble(k);
}

if (q == void) {
    //set a default value if no value is passed in.
    q = 4;
} else {
    //read in the passed in value; the passed in value is of type String and may require conversion.
    q = Integer.parseInt(q);
}
```
