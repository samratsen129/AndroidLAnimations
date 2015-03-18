package com.example.sam.androidlanimations.androidlanimations.model;


public class Data1 {

    private String name;
    private String nameDesc;
    private int imageId;

    public Data1(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
        nameDesc= name +", " + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImagePath() {
        return imageId;
    }

    public void setImagePath(int imageId) {
        this.imageId = imageId;
    }

    public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }

}
