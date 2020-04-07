# This file is only intended for testing purposes to read the values from the sensors and act accordingly

import components

# To test the moisture sensor, uncomment the followingline
# print(moistureLevel())

# To test the UV Sensor, uncomment the following line
print(lightingLevel())

# watering()

# Just for testingpurposes 
# print(components.dry())
while True : 	
	while (components.dry() == True):
		components.watering()
			
