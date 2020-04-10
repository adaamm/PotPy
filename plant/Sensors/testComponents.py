# This file is only intended for testing purposes to read the values from the sensors and act accordingly

import components

# To test the moisture sensor, uncomment the following line
# print(moistureLevel())

# To test the UV Sensor, uncomment the following line
print(lightingLevel())

# watering()

# Just for testing purposes
# print(components.dry())
while True:
    while components.dry():
        components.watering()
