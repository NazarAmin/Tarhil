package com.example.higooo.tarhil;

public class Trip {
    int ID;
    int AvgS;
int MaxS;
int DisS;
String StartS;
String EndS;

    public Trip(int id, int avgS, int maxS, int disS, String startS, String endS) {
        ID = id;
        AvgS = avgS;
        MaxS = maxS;
        DisS = disS;
        StartS = startS;
        EndS = endS;
    }

    public Trip() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public int getAvgS() {
        return AvgS;
    }

    public void setAvgS(int avgS) {
        AvgS = avgS;
    }

    public int getMaxS() {
        return MaxS;
    }

    public void setMaxS(int maxS) {
        MaxS = maxS;
    }

    public int getDisS() {
        return DisS;
    }

    public void setDisS(int disS) {
        DisS = disS;
    }

    public String getStartS() {
        return StartS;
    }

    public void setStartS(String startS) {
        StartS = startS;
    }

    public String getEndS() {
        return EndS;
    }

    public void setEndS(String endS) {
        EndS = endS;
    }
}
