# * Imports
# 3rd Party Imports
# User Imports

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
    def __init__(self, name, type_, sensorController):
        self.name = name
        self.type = type_    

        self.phLevel = 0
        self.lightLevel = 0
        self.moistureLevel = 0
        self.temperatureLevel = 0

        self.sensorController = sensorController
        
    def set_moisture(self):
        self.moistureLevel = self.sensorController.get_moisture()

    def set_light(self):
        self.lightLevel = self.sensorController.get_light()

    def set_ph(self):
        print("under construction")

    def set_temperature(self):
        print("under construction")

    def set_all_data(self):
        self.set_moisture()
        self.set_light()
        self.set_ph()
        self.set_temperature()

    def get_all_data(self):
        plantData = {
            "ph": self.phLevel,
            "light": self.lightLevel,
            "moisture": self.moistureLevel,
            "temperature": self.temperatureLevel
        }
        return plantData