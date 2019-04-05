package com.example.higooo.tarhil;

public class UserLocation {
    String id;
    String name;
    double longi;
    double latti;

    public UserLocation() {
    }


    public UserLocation(double longi, double latti) {
        this.longi = longi;
        this.latti = latti;
    }

    public UserLocation(String id, String name, double longi, double latti) {
        this.id = id;
        this.name = name;
        this.longi = longi;
        this.latti = latti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLatti() {
        return latti;
    }

    public void setLatti(double latti) {
        this.latti = latti;
    }
}
