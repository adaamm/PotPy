package com.example.a390application.InsertPlant;

//This Object is used to store the data concerning a Raspberry PI for further usage.
public class PI {

    private String PlantName;
    private String OwnerID;
    private String Password;
    private String imgUpdate;
    private String image;

    public PI(String PlantName, String id, String pw, String imgu, String img) {
        this.PlantName = PlantName;
        OwnerID = id;
        Password = pw;
        imgUpdate = imgu;
        image = img;
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

    public String getImgUpdate() {
        return imgUpdate;
    }

    public void setImgUpdate(String imgUpdate) {
        this.imgUpdate = imgUpdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}