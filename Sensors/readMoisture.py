# This code is used to read the Moisture sensor's data 
# The data output by the moisture sensor is from 0 to 1023 where 1023 means complete dryness


from numpy import interp # For linear interpolation
from time import sleep 
import spidev 

# py-spidev provides a python module for interfacing with SPI devices 
# Get py-spidev from git clone https://github.com/doceme/py-spidev.git     
spi = spidev.SpiDev()  
spi.open(0,0)                                                     

def analogInput(channel):          #readchannel
        spi.max_speed_hz = 1350000
        adc = spi.xfer2([1,(8+channel)<<4,0])
       # print("raw data ",adc)
        data = ((adc[1]&3)<<8)+adc[2]
        return data 
        
        
def level():
        output = analogInput(0) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        moisture = int(output)
        # Uncomment following line to test Moisture Sensor (with water or soil)
        # print("Moisture: ", output)
        return moisture; 
