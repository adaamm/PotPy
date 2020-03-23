# This code is used to read the values from the sensors and act accordingly

import components

# Just for testingpurposes 
# print(components.dry())
while True : 	
	while (components.dry() == True):
		components.watering()

import time
