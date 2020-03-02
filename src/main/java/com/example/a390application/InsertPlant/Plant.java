package com.example.a390application.InsertPlant;

public class Plant {

    private Integer ID;
    private String Name;
    private String Type;
    private double Moisture;
    private double Ph;

    public Plant(Integer ID, String name, String type, double moisture, double ph) {
        this.ID = ID;
        Name = name;
        Type = type;
        Moisture = moisture;
        Ph = ph;
    }


    public Plant(String name, String type) {
        Name = name;
        Type = type;
        Moisture = -1;
        Ph = -1;
    }

    public Plant(String name, String type, double moisture, double ph) {
        Name = name;
        Type = type;
        Moisture = moisture;
        Ph = ph;
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

    public double getPh() {
        return Ph;
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

    public void setPh(double ph) {
        Ph = ph;
    }
}
