package com.example.a390application.InsertPlant;

public class Plant {

    private Integer ID;
    private String Name;
    private String Type;
    private double Moisture;
    private double LightIntensity;
    private String Test;

    public Plant(Integer ID, String name, String type, double moisture, double lightIntensity, String test) {
        this.ID = ID;
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Test = test;
    }


    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    public Plant(String name, String type) {
        Name = name;
        Type = type;
        Moisture = -1;
        LightIntensity = -1;
        Test = "default";
    }

    public Plant(String name, String type, double moisture, double lightIntensity, String test) {
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Test = test;
    }


    public Integer getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public double getMoisture() {
        return Moisture;
    }

    public double getLightIntensity() {
        return LightIntensity;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMoisture(double moisture) {
        Moisture = moisture;
    }

    public void setLightIntensity(double lightIntensity) {
        LightIntensity = lightIntensity;
    }
}
