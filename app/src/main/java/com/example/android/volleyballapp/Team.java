package com.example.android.volleyballapp;

public class Team {
    private String name;
    private String type;
    private String season;

    Team(String n, String t, String s){
        name = n;
        type = t;
        season = s;
    }
    public void addTeam(){
        //add team to database
    }
    public void getTeam(){
        //get team from database;
    }
    public void setName(String n){
        name = n;
    }
    public void setType(String t){
        type = t;
    }
    public void setSeason(String s){
        season = s;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getSeason(){
        return season;
    }
}
