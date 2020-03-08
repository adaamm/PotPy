 # * author : Philippe Vo 
 # * date : Mar-07-2020 12:32:55
 
# * Imports
# 3rd Party Imports
import time
# User Imports
from firebase.firebase_plant_com import FirebasePlantCom
from sensors.components import *

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
    
    # Gather data and send 2 Firebase
    print("Gathering data from sensors and sending to Firebase ...")
    try:
        while True:
            time.sleep(0.1)
            moistureData = components.moistureLevel()
            firebaseCOM.update("Plant1","moisture",moistureData)
            print("Updating moisture data : " + str(moistureData))

    except KeyboardInterrupt:
        print('interrupted!')

if __name__ == '__main__':
    main()