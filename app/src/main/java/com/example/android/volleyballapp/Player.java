package com.example.android.volleyballapp;


/*
Player object will hold Player name, number, and related methods
 */
public class Player {
    private int ID;
    private String firstName;
    private String lastName;
    private int number;
    private String gradeLevel;

    //also need variable to hold either picture or picture location so
    //that it can be displayed

    //constructor
    Player(int id, String fN, String lN, int num, String gL){
        ID = id;
        firstName = fN;
        lastName = lN;
        number = num;
        gradeLevel = gL;
    }
    Player(String fN, String lN, int num, String gL){
        firstName = fN;
        lastName = lN;
        number = num;
        gradeLevel = gL;

        //will need to also assign picture information in constructor as well
    }

    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    //getters
    public String getFirstName(){
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNumber() {
        return number;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

}
