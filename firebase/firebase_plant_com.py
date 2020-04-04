# * author : Philippe Vo 
# * date : Feb-28-2020 09:16:42
 
# * Imports
# 3rd Party Imports
import pyrebase
import time
# User Imports

# * Code
class FirebasePlantCom:
    """
    communication component to send and read data from our firebase app

    we are assuming that the data itself looks like this

    {
        Plant1 : {
        moisture : 
        name : 
        ph : 
        type : 
        }
    }
    """
    def __init__(self):
        self.config = {
        "apiKey": "AIzaSyAeLC1SPoeniyf98IpanvSTzFc_Yh_DS1w",
        "authDomain": "coen390-guarduino.firebaseapp.com",
        "databaseURL": "https://coen390-guarduino.firebaseio.com/",
        "storageBucket": "coen390-guarduino.appspot.com"
        }

        print("Signing in to Firebase ...")
        self.firebase = pyrebase.initialize_app(self.config)
        self.auth = self.firebase.auth()
        self.user = self.auth.sign_in_with_email_and_password("philippe.vo.nam@gmail.com", "secretPassword555") # you need to make a user in Firebase first (under Authentication)
        self.db = self.firebase.database()

        # before the 1 hour expiry:
        self.user = self.auth.refresh(self.user['refreshToken']) # need this or we will have keyError

    def update(self, plant, dataType, data):
        """
        update data to firebase for a specific plant

        Parameters
        ----------
        plant : str
            name of the plant id
        dataType : str
            type of data we want to write (moisture, name, ph, type)
        data : str
            data itself

        Returns
        -------
        """
        self.db.child(plant).update({dataType: data})

    def update_pi_data(self, piID, plantData):
        """
        update the pi on firebase given the piID

        Parameters
        ----------
        piID : int
            name of the pi id
        plantData : dict
            type of data we want to write (moisture, name, ph, type)

        Returns
        -------
        """
        # self.db.child(plant).update({"ph": plantData["ph"]})
        self.db.child(piID).update({"light": plantData["light"]})
        self.db.child(piID).update({"moisture": plantData["moisture"]})
        self.db.child(piID).update({"temperature": plantData["temperature"]})
        self.db.child(piID).update({"humidity": plantData["humidity"]})
    
    def read(self, plant, dataType):
        """
        read data to firebase for a specific plant

        Parameters
        ----------
        plant : str
            name of the plant id
        dataType : str
            type of data we want to write (moisture, name, ph, type)

        Returns
        -------
        data : str
            data requested
        """
        if self.check_plant_exist(plant):
            plant = self.db.child(plant).get()
            data = plant.val()[dataType]

            return data

    def check_plant_exist(self, plant):
        """
        check if the plant exists in the database

        Parameters
        ----------
        plant : str
            name of the plant id

        Returns
        -------
        response : boolean
            query response
        """
        try:
            plantData = self.db.child(plant).get()
            data = plantData.val()
            data["test"]
            return True
        except TypeError:
            print(plant + " -> does not exist in our firebase")
            return False