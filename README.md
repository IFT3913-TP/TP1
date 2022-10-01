# Build & Run instructions
### Requirements
- *Java Development Kit (version 8 min)*
- *GNU make*

___
**All commands below are supposed to be run from the root folder of the project**

## Build instructions
To build the whole project run:

```Shell
make all
```

To build a specific part (for example *PARTIE_0*) run:

```Shell
make PARTIE_0
```

## Run Instructions
After building you can for example launch *jls.jar* from *PARTIE_0* with : 

```Shell
java -jar PARTIE_0/out/jls.jar PATH_TO_DIRECTORY
```
You can also pipe between programs 
```Shell
java -jar PARTIE_0/out/jls.jar ../../ckjm/src/ | java -jar PARTIE_2/out/lcsec.jar | java -jar PARTIE_3/out/egon.jar 40
```
