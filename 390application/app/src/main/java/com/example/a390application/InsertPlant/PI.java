package com.example.a390application.InsertPlant;

public class PI {

    private String PlantName;
    private String OwnerID;

    public PI(String PlantName, String id) {
        this.PlantName = PlantName;
        OwnerID = id;
    }

    public String getName() {
        return PlantName;
    }

    public void setName(String name) {
        PlantName = name;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }
}