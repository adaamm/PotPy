
# * Imports
# 3rd Party Imports
import os
import RPi.GPIO as GPIO
import time 
from numpy import interp # For linear interpolation
import spidev 
# User Imports

class SensorController():
    def __init__(self, moisturePin, lightPin, phPin):
        self.pinMap = {
            "moisturePin": moisturePin,
            "lightPin": lightPin,
            "phPin": phPin
        }
        
        # py-spidev provides a python module for interfacing with SPI devices 
        # Get py-spidev from git clone https://github.com/doceme/py-spidev.git 
        self.spi = spidev.SpiDev()  
        self.spi.open(0,0)     

    def analogInput(self,channel):          
        self.spi.max_speed_hz = 1350000
        adc = self.spi.xfer2([1,(8+channel)<<4,0])
        data = ((adc[1]&3)<<8)+adc[2]
        return data 
        
    def get_moisture(self):
        output = analogInput(self.pinMap["moisture"]) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        moisture = int(output)
        # Uncomment following line to test Moisture Sensor (with water or soil)
        # print("Moisture: ", output)
        return moisture
            
    def get_light(self):
        output = analogInput(self.pinMap["lightPin"]) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        uv = int(output)
        return uv; 

    def get_ph(self):
        print("under construction")

    def get_temperature(self):
        print("under construction")

    def dry(self):
        if moistureLevel() >= 900:
            return True
        else:
            return False

    def watering(self):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(18, GPIO.OUT)
        GPIO.output(18, GPIO.LOW)
        time.sleep(2)
        GPIO.output(18,GPIO.HIGH)
        time.sleep(2)
