package com.shayaanfarooq.architech.model;


//CURRENTLY NO FINAL JUST PROTOTYPING
public class FloorPlan {
    String title;
    String owner;
    String width;
    String length;
    String noOfCars;
    String bathrooms;
    String bedrooms;
    String id;
    String ownerName;


    public FloorPlan()
    {
        this.id=null;
        this.title= null;
        this.owner= null;
        this.width= null;
        this.length=null;
        this.noOfCars=null;
        this.bathrooms=null;
        this.bedrooms=null;
        this.ownerName = null;
    }

    public FloorPlan(String title, String owner, String width, String length, String noOfCars, String bathrooms, String bedrooms,
                     String id,String ownerName) {
        this.title = title;
        this.owner = owner;
        this.width = width;
        this.length = length;
        this.noOfCars = noOfCars;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.id=id;
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getNoOfCars() {
        return noOfCars;
    }

    public void setNoOfCars(String noOfCars) {
        this.noOfCars = noOfCars;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void display()
    {
        System.out.println("Title:"+ title);
        System.out.println("Size"+ width + " x " + length);
        System.out.println("by: "+ owner);

    }
}
