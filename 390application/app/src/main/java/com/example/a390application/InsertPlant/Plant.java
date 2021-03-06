package com.example.a390application.InsertPlant;

//This Object is used upon creation to store the data concerning a plant for further usage.
public class Plant {

    private Integer ID;
    private String Name;
    private String Type;
    private double Moisture;
    private double LightIntensity;
    private double Humidity;
    private double Temperature;
    private String OwnerID;

    Plant(Integer ID, String name, String type, double moisture, double lightIntensity, double humidity, double temperature, String id) {
        this.ID = ID;
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Humidity = humidity;
        Temperature = temperature;
        OwnerID = id;
    }

    public Plant(String name, String type, String id) {
        Name = name;
        Type = type;
        Moisture = -10000;
        LightIntensity = -10000;
        Humidity = -10000;
        Temperature = -10000;
        OwnerID = id;
    }

    public Plant(String name, String type, double moisture, double lightIntensity, double humidity, double temperature, String id) {
        Name = name;
        Type = type;
        Moisture = moisture;
        LightIntensity = lightIntensity;
        Humidity = humidity;
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

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double Humidity) {
        this.Humidity = Humidity;
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
