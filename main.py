 # * author : Philippe Vo 
 # * date : Mar-07-2020 12:32:55
 
# * Imports
# 3rd Party Imports
import time
# User Imports
from firebase.firebase_plant_com import FirebasePlantCom
from sensors.sensor_contoller import SensorController
from plant.plant import Plant

# * Template
"""
one sentence description

Parameters
----------
id : str
    one sentence description

Returns
-------
id : str
    one sentence description
"""

# * Code
def main():
    """
    starting point of the code
    """
    print("Starting Pot-O-Duino ...")
    
    # Sign in 2 Firebase
    firebaseCOM = FirebasePlantCom()

    # Make Plant and its SensorController
    sensorController1 = SensorController(moisturePin=0, lightPin=1, phPin=-1)
    plant1 = Plant(name="Plant1", type="Aloe", sensorController=sensorController1)
    
    # Gather data and send 2 Firebase
    print("Gathering data from sensors and sending to Firebase ...")
    try:
        while True:
            time.sleep(0.1)
            plant1.set_all_data()
            plantData = plant1.get_all_data()
            firebaseCOM.update_plant(plant1.name,plantData)
            print("Updating data : " + plantData)

    except KeyboardInterrupt:
        print('interrupted!')

if __name__ == '__main__':
    main()