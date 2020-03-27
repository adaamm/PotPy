# * Imports
# 3rd Party Imports
# User Imports
from plant.plant_need import PlantNeedJSON, MissingPlantType

class Plant():
    """
    plant with data about its wellbeing

    Parameters
    ----------
    name : str
        name of the plant 
    type_ : str
        type of plant
    sensorController : SensorController
        controller for the sensors that is implemented on the plant 
    """
    def __init__(self, name, type_, sensorController = ""):
        self.name = name
        self.type_ = type_

        self.phLevel = 0
        self.lightLevel = 0
        self.moistureLevel = 0
        self.temperatureLevel = 0
        self.humidityLevel = 0 

        # Values that we will "act" on
        self.moistureThresholdLevel = 0

        # Values to keep track for the health of the plant
        self.phThresholdLevel = 0
        self.lightThresholdLevel = 0
        self.temperatureThresholdLevel = 0

        # Set all the threshold values
        self.setThresholdData()

        self.sensorController = sensorController

    def print_plant(self):
        print("---")
        print("plant :" + self.name)
        print("type :" + self.type_)
        print("ph :" + str(self.phLevel))
        print("light :" + str(self.lightLevel))
        print("moisture :" + str(self.moistureLevel))
        print("temperature :" + str(self.temperatureLevel))
        print("humidity :" + str(self.humidityLevel))
        print("---")
        

    def set_moisture(self):
        self.moistureLevel = self.sensorController.get_moisture()

    def set_light(self):
        self.lightLevel = self.sensorController.get_light()

    def set_ph(self):
        #print("under construction")
        pass
        
    def dry(self):
        if self.moistureLevel >= 900:
            return True
        else:
            return False

    def set_temperature(self):
        self.temperatureLevel = self.sensorController.get_temperature()
        
    def set_humidity(self):
        self.humidityLevel = self.sensorController.get_humidity()

    def set_all_data(self):
        self.set_moisture()
        self.set_light()
        self.set_ph()
        self.set_temperature()
        self.set_humidity()

    def get_all_data(self):
        plantData = {
            "ph": self.phLevel,
            "light": self.lightLevel,
            "moisture": self.moistureLevel,
            "temperature": self.temperatureLevel,
            "humidity":self.humidityLevel
        }
        return plantData

    def setThresholdData(self):
        """
        retrieve data of the plant's need based on its needs from json db

        Arguments
        ---------

        Returns
        ------
        """
        try:
            plantDataJSON = PlantNeedJSON()
            plantData = plantDataJSON.read_type_need(self.type_)

            self.moistureThresholdLevel = plantData["moistureTLevel"]
            self.lightThresholdLevel = plantData["lightTLevel"]
            self.temperatureThresholdLevel = plantData["temperatureTLevel"]

        except MissingPlantType:
            print("Please add plant type needs")
            plantDataJSON.add_type_need(self.type_)
            self.setThresholdData() # retry to set the data
