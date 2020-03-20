
import os
import smbus2 
import bme280 

import RPi.GPIO as GPIO
import time 

from numpy import interp # For linear interpolation
from time import sleep 
import spidev 

# py-spidev provides a python module for interfacing with SPI devices 
# Get py-spidev from git clone https://github.com/doceme/py-spidev.git     
spi = spidev.SpiDev()  
spi.open(0,0) 

#following lines are needed to contact  the atmospheric sensor
port= 1
address = 0x77  # cchecked with i2cdetect - y 1 
bus  = smbus2.SMBus(port)
calibration_params = bme280.load_calibration_params(bus,address)
data = bme280.sample(bus,address,calibration_params) 

#this function is needed to read input from mpc3008
#we have 4 channels and number of the channel should be passed to the functons
def analogInput(channel):          #readchannel
        spi.max_speed_hz = 1350000
        adc = spi.xfer2([1,(8+channel)<<4,0])
       # print("raw data ",adc)
        data = ((adc[1]&3)<<8)+adc[2]
        return data 
	
# moisture related functions
def moistureLevel():
        output = analogInput(0) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        moisture = int(output)
        # Uncomment following line to test Moisture Sensor (with water or soil)
        # print("Moisture: ", output)
        return moisture
	
def dry():
	if moistureLevel() >= 900:
		return True
	else:
		return False

def watering():
	GPIO.setwarnings(False)
	GPIO.setmode(GPIO.BCM)
	GPIO.setup(18, GPIO.OUT)
	GPIO.output(18, GPIO.LOW)
	time.sleep(2)
	GPIO.output(18,GPIO.HIGH)
	time.sleep(2)
        
        
def lightingLevel():
        output = analogInput(1) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        uv = int(output)
        return uv; 

#return humidity
def humidity():
	return data.humidity;
	
def pressure():
	pressure = data.pressure / 10 ; 
	return pressure ; 
	
	
		



# To test the moisture sensor, uncomment the followingline
print(moistureLevel())

# To test the UV Sensor, uncomment the following line
print(lightingLevel()) 

# watering()

