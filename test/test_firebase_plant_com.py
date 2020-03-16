 # * author : Philippe Vo 
 # * date : 2020-March-07 17:38:12
 
# * Imports
# 3rd Party Imports
import unittest
# User Imports
from firebase.firebase_plant_com import FirebasePlantCom

# NOTE : cmd to run for testing (need to be Pot-O-duino dir first) : python -m unittest -v test.test_firebase_plant_com

class FirebasePlantComTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        """ called only once, before any test in the class """
        print("Testing firebase_plant_com")
        cls.firebase = FirebasePlantCom()

    def test_read(self):
        data = self.firebase.read("TestPlant","test")
        self.assertEqual(data, "default")

    def test_check_plant_exist(self):
        data = self.firebase.check_plant_exist("FalsePlant") # assuming we dont have "FalsePlant" as a plant
        self.assertFalse(data)

# if __name__ == '__main__':
unittest.main()