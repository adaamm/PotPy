# This code is used to read the UV sensor's data 
# The data output by the UV sensor is from 0 to 11+ dryness



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
        output = analogInput(1) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        uv = int(output)
        return uv; 
