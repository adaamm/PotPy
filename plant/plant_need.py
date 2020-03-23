#!/usr/bin/env python3

# 3rd Party Imports
import json

class PlantNeedJSON():
    def __init__(self):
        """
        init. of the plant need json class
        """
        self.dbFile = "plant_need.json"

    def checkTypeExist(self, type_):
        """
        check it the type of plant is already in the database of plant needs

        Arguments
        --------
        type_ : str
            the plant type

        Returns
        -------
        """
        try:
            with open(self.dbFile) as datafile:
                data = json.load(datafile)
                types = []
                for p in data['plant']:
                    types.append(p['type'])

            if type_ in types:
                return True

            else:
                return False

        except json.decoder.JSONDecodeError:
            print("database json file not built yet")
            return False

    def readTypeNeed(self, type_):
        """
        read plant needs of the type of plant that is already in the database of plant needs

        Arguments
        --------
        type_ : str
            the plant type

        Returns
        -------
        plantNeeds : dict
            data of the plant needs
        """
        if self.checkTypeExist(type_):
            with open(self.dbFile) as datafile:
                data = json.load(datafile )
                for plant in data['plant']:
                    if plant['type'] == type_:
                        plantNeeds = {
                            'moistureTlevel': plant['moistureTLevel'],
                            'temperatureTLevel': plant['temperatureTLevel'],
                            'lightTLevel': plant['lightTLevel']
                        }

                return plantNeeds

        else:
            print("Plant Type is not in database yet")
            raise MissingPlantType

    def addTypeNeed(self, type_):
        """
        add the type of plant in the database of plant needs

        Arguments
        ---------
        type_ : str
            the plant type

        Returns
        -------

        """
        if not self.checkTypeExist(type_):
            moistureTLevel = input("input moisture threshold value : ")
            temperatureTLevel = input("input temperature threshold value : ")
            lightTLevel = input("input light threshold value : ")

            data = {}
            data['plant'] = []
            data['plant'].append({
                'type': type_,
                'moistureTLevel': moistureTLevel,
                'temperatureTLevel': temperatureTLevel,
                'lightTLevel': lightTLevel
            })

            # append to db json file
            with open(self.dbFile, 'a+') as datafile:
                json.dump(data, datafile, indent = 4)

        else:
            print("Plannt type already exists in database")

# SECTION : Exceptions
# define Python user-defined exceptions
class Error(Exception):
    """Base class for other exceptions"""
    pass

class MissingPlantType(Error):
    """Raised when the input value for electrode is not within range"""
    pass

if __name__ == '__main__':
    plantJSON = PlantNeedJSON()
    print(plantJSON.readTypeNeed("cactus"))
