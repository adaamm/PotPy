
# * Imports
# 3rd Party Imports
import time

import RPi.GPIO as GPIO
import bme280
import smbus2
# from numpy import interp # For linear interpolation
# from time import sleep #uncomment when need to use a delay
import spidev  # Needed for SPI programming see comments below

# User Imports
# from components import *  #imports all functions from component


# The following lines are used for the i2c pins (sda scl ) of the PI
# The I2C pins are connected to the bme280 but can be connected with more
port = 1
# the following is the address of the BME280 sensor
address = 0x77  # checked with i2cdetect - y 1 in command line
bus = smbus2.SMBus(port)
calibration_params = bme280.load_calibration_params(bus, address)
data = bme280.sample(bus, address, calibration_params)

# Voltages read by the uv analog sensor
# Declared here so that it is not defined everytime the function is called
uv_voltages = [50, 227, 318, 408, 503, 606, 696, 795, 881, 976, 1079, 1170];

class SensorController():
    def __init__(self, moisture, light, ph, humidity, temperature):
        self.pinMap = {
            "moisturePin": moisture,
            "lightPin": light,
            "phPin": ph,
            "humidityPin": humidity,
            "temperaturePin": temperature
        }
    
        # py-spidev provides a python module for interfacing with SPI devices
        # Get py-spidev from git clone https://github.com/doceme/py-spidev.git
        # Activating the SPI to use with the MCP3008 and connect analog sensors 
        self.spi = spidev.SpiDev() 
        self.spi.open(0,0)
        

    # Reading input from MCP3008
    def analogInput(self,channel):
        self.spi.max_speed_hz = 1350000
        adc = self.spi.xfer2([1,(8+channel)<<4,0])
        data = ((adc[1]&3)<<8)+adc[2]
        return data

    def get_moisture(self):
        output = self.analogInput(self.pinMap["moisturePin"]) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed 
        moisture = int(output)
        # Uncomment following line to test Moisture Sensor (with water or soil)
        # print("Moisture: ", output)
        return moisture
            
    def get_light(self):
        output = self.analogInput(self.pinMap["lightPin"]) 
        # Note that uncommenting the following line will change the range of numbers outputted
        #output = interp(output,[0,1023],[100,0]) # interpolate only if needed
        sensor_voltage = int(output) / 1024 * 3.3
        uv = uv_voltages.index(closest(uv_voltages, sensor_voltage))
        return uv; 

    def get_ph(self):
       print("under construction")
       pass

    def get_temperature(self):
      return data.temperature;

    def get_humidity(self):
        return data.humidity;

    def get_pressure(self):
        pressure = data.pressure / 10;  # Pressure is returned in hPa from the sensor, we divide to convert
        return pressure;


    def watering(self):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(18, GPIO.OUT)
        GPIO.output(18, GPIO.LOW)
        time.sleep(2)
        GPIO.output(18,GPIO.HIGH)
        time.sleep(2)
# Python program to find the closest number in a list
def closest(lst, K):
    return lst[min(range(len(lst)), key=lambda i: abs(lst[i] - K))]