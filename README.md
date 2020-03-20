# Pot-O-Duino
COEN/ELEC 390 Team Engineering Project

# The Sensors 
## Moisture Sensor
![Image of Moisture Sensor](images/moisture.jpg?raw=true)

The moisture sensor reads value from 0 (wet) to 1023 (dry). 
## UV light Sensor
![Image of Light Sensor](images/uvsensor.jpg?raw=true)
The output voltage of the UV sensor changes with the intensity of the sun. 
According to the voltage, the output of the sensor gives us the UV index. 
![UVindex](images/UVindex.png?raw=true "UVindex")

## MPC3008 
This is used as an interface between the Analog pins of the sensors and the digital pins
of the Raspberry Pi 3B. To use the MPC3008, we need an SPI library which we get from py-spidev by [doceme](https://github.com/doceme/py-spidev) on Github.

#### The connections are as follow
![Connections](images/fritzingconnections.png)
