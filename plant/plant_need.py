#!/usr/bin/env python3

# 3rd Party Imports
import json
import os

class PlantNeedJSON():
    def __init__(self):
        """
        init. of the plant need json class
        """
        dirname = os.path.dirname(__file__)
        filename = os.path.join(dirname, 'plant_need.json')
        self.dbFile = filename

    def check_type_exist(self, type_):
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

        except json.decoder.JSONDecodeError as e:
            print(e)
            print("database json file not built yet")
            return False

    def read_type_need(self, type_):
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
        if self.check_type_exist(type_):
            with open(self.dbFile) as datafile:
                data = json.load(datafile)
                for plant in data['plant']:
                    if plant['type'] == type_:
                        plantNeeds = {
                            'moistureTLevel': plant['moistureTLevel'],
                            'temperatureTLevel': plant['temperatureTLevel'],
                            'lightTLevel': plant['lightTLevel']
                        }

                return plantNeeds

        else:
            print("Plant Type is not in database yet")
            raise MissingPlantType

    def add_type_need(self, type_):
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

            # append to db json file
            with open(self.dbFile, 'r+') as datafile:
                data = json.load(datafile)
                data['plant'].append({
                    'type': type_,
                    'moistureTLevel': moistureTLevel,
                    'temperatureTLevel': temperatureTLevel,
                    'lightTLevel': lightTLevel
                })
                datafile.seek(0) # erase the previous data
                datafile.truncate()
                json.dump(data, datafile, indent = 4)

        else:
            print("Plannt type already exists in database")

    def create_db_json(self):
        """
        add the type of plant in the database of plant needs

        Arguments
        ---------
        type_ : str
            the plant type

        Returns
        -------

        """
        data = {}
        data['plant'] = []
        data['plant'].append({
            'type': 'default',
            'moistureTLevel': 0,
            'temperatureTLevel': 0,
            'lightTLevel': 0
        })

        # append to db json file
        with open(self.dbFile, 'a+') as datafile:
            json.dump(data, datafile, indent = 4)

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
    plantJSON.create_db_json()
    # plantJSON.add_type_need("cactus")
