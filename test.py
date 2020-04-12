 # * author : Philippe Vo 
 # * date : Mar-07-2020 12:32:55
 
# * Imports
# 3rd Party Imports
import time
# User Imports
from firebase.firebase_plant_com import FirebasePlantCom
# from plant.Sensors.sensor_controller import SensorController
from plant.plant import Plant

from ai.img_plant_controller import run_img_guessing

# *Code
class MainRun:
    def __init__(self):
        """
        init.
        """
        self.firebaseCOM = FirebasePlantCom(piID = "123")

        # setinng up listener for image of plant
        self.imgUpdatePiIDPath = self.firebaseCOM.get_image_path_update()
        self.imgPiIDPath = self.firebaseCOM.get_image_path()

        my_stream = self.firebaseCOM.db.child(self.imgUpdatePiIDPath).stream(self.stream_img_handler)

    # NOTE : NEW
    def stream_img_handler(self, message):
        """
        monitors the imgs that gets taken and activates a function when there is a change
        """
        data = self.firebaseCOM.db.child(self.imgPiIDPath).get()
        base64Img =  data.val()
        plantType = run_img_guessing(base64Img)
        print(plantType)

    # * Code
    def main(self):
        """
        starting point of the code
        """
        print("Starting Pot-O-Duino ...")

        # Sign in 2 Firebase
        # self.firebaseCOM = FirebasePlantCom(piID = "123")

        # Make Plant and its SensorController
        # sensorController1 = SensorController(moisture=0, light=1, ph=-1, humidity = 0, temperature = 0)

        plant0 = Plant(name=self.firebaseCOM.plantName, type_="Aloe", sensorController="")

        # imgPiIDPath = "Users/-M4fSQFTJYiPq3lxwT0o/ImageImageImage"
        # data = self.firebaseCOM.db.child(imgPiIDPath).get()
        # print(data.val()[0:10])

        # plantType = run_img_guessing(data.val())


        level = 0
        # Gather data and send 2 Firebase
        print("Gathering data from sensors and sending to Firebase ...")
        try:
            while True:
                time.sleep(0.01)
                # plant0.set_all_data()
                # plantData = plant0.get_all_data()
                level = level + 2
                plantData = {
                    "light": level,
                    "moisture": 1,
                    "temperature": 1,
                    "humidity": 1
                }
                piID = 1
                piIDPath = self.firebaseCOM.get_firebasePlantPath()
                # print(piIDPath)
                self.firebaseCOM.update_pi_data(piIDPath, plantData)
                # plant0.print_plant()

        except KeyboardInterrupt:
            print('interrupted!')


if __name__ == '__main__':

    mainRun = MainRun()

    mainRun.main()
