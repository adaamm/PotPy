## The following contains all of the functions needed to communicate with the components

import time

import RPi.GPIO as GPIO
import bme280
# import os
import smbus2
# from numpy import interp # For linear interpolation
# from time import sleep #uncomment when need to use a delay
import spidev  # Needed for SPI programming see comments below

# py-spidev provides a python module for interfacing with SPI devices 
# Get py-spidev from git clone https://github.com/doceme/py-spidev.git
# Activating the SPI to use with the MCP3008 and connect analog sensors (see connections.fpz )
spi = spidev.SpiDev()
spi.open(0, 0)

# The following lines are used for the i2c pins (sda scl ) of the PI
# The I2C pins are connected to the bme280 but can be connected with more
port = 1
# the following is the address of the BME280 sensor
address = 0x77  # checked with i2cdetect - y 1 in command line
bus = smbus2.SMBus(port)
calibration_params = bme280.load_calibration_params(bus, address)
data = bme280.sample(bus, address, calibration_params)


# this function is needed to read input from mpc3008
# we have 4 channels and number of the channel should be passed to the function
def analogInput(channel):
    spi.max_speed_hz = 1350000
    adc = spi.xfer2([1, (8 + channel) << 4, 0])
    # print("raw data ",adc)
    data = ((adc[1] & 3) << 8) + adc[2]
    return data

# moisture related functions
def moistureLevel():
    output = analogInput(0)
    # Note that uncommenting the following line will change the range of numbers outputted
    # output = interp(output,[0,1023],[100,0]) # interpolate only if needed
    moisture = int(output)
    # Uncomment following line to test Moisture Sensor (with water or soil)
    # print("Moisture: ", output)
    return moisture


def dry():
    if moistureLevel() >= 900:
        return True
    else:
        return False

# The following line activates the water pump and water is delivered into the plant for 2 seconds every 2 seconds.
def watering():
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(18, GPIO.OUT)
    GPIO.output(18, GPIO.LOW)
    time.sleep(2)
    GPIO.output(18, GPIO.HIGH)
    time.sleep(2)

# The following returns the UV light index in the room.
def lightingLevel():
    output = analogInput(1)
    # Note that uncommenting the following line will change the range of numbers outputted
    # output = interp(output,[0,1023],[100,0]) # interpolate only if needed
    uv_voltages = [50,227,318,408,503,606,696,795,881,976,1079,1170]
    sensor_voltage = int(output)/1024*3.3
    uv = (uv_voltages.index(closest(uv_voltages,sensor_voltage)))+1
    return uv;


# The following function returns the percentage of humidity in the room
def humidity():
    return data.humidity;

# The following function returns the pressure in the room in kPa
def pressure():
    pressure = data.pressure / 10; # Pressure is returned in hPa from the sensor, we divide to convert
    return pressure;

# Python program to find the closest number in a list
def closest(lst, K):
    return lst[min(range(len(lst)), key=lambda i: abs(lst[i] - K))]
