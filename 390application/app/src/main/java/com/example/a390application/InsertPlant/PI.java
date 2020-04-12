package com.example.a390application.InsertPlant;

public class PI {

    private String PlantName;
    private String OwnerID;
    private String Password;



    public PI(String PlantName, String id, String pw) {
        this.PlantName = PlantName;
        OwnerID = id;
        Password = pw;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}