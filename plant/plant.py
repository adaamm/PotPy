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
        self.humidityLevel = 0 

        self.sensorController = sensorController
        
    def print_plant(self):
        print("---")
        print("plant :" + self.name)
        print("type :" + self.type)
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
        plantdata = {
            "ph": self.phLevel,
            "light": self.lightLevel,
            "moisture": self.moistureLevel,
            "temperature": self.temperatureLevel,
            "humidity":self.humidityLevel
        }
        return plantdata
