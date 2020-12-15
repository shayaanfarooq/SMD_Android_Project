package com.shayaanfarooq.architech.model;

public class Image {
    String name;
    String floorPlanId;

    public Image(String name, String floorPlanId) {
        this.name = name;
        this.floorPlanId = floorPlanId;
    }
    public Image() {
        this.name=null;
        this.floorPlanId=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }


}

