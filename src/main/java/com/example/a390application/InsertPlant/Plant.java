package com.example.a390application.InsertPlant;

public class Plant {

    private Integer ID;
    private String Name;
    private String Type;
    private double Moisture;
    private double LightIntensity;
    private String Test;
    private double PH;
    private double Temperature;
    private String OwnerID;

    public Plant(Integer ID, String name, String type, double moisture, double lightIntensity, String test, double ph, double temperature, String id) {
        this.ID = ID;
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Test = test;
        PH = ph;
        Temperature = temperature;
        OwnerID = id;
    }

    public Plant(String name, String type, String id) {
        Name = name;
        Type = type;
        Moisture = -1;
        LightIntensity = -1;
        Test = "default";
        PH = -1;
        Temperature = -1;
        OwnerID = id;

    }

    public Plant(String name, String type, double moisture, double lightIntensity, String test, double ph, double temperature, String id) {
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Test = test;
        PH = ph;
        Temperature = temperature;
        OwnerID = id;
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

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    public double getPH() {
        return PH;
    }

    public void setPH(double PH) {
        this.PH = PH;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }
}
