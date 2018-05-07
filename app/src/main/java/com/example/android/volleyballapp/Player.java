package com.example.android.volleyballapp;


import android.os.Parcelable;

import java.io.Serializable;

/*
Player object will hold Player name, number, and related methods
 */
public class Player implements Serializable{
    private int ID;
    private String firstName;
    private String lastName;
    private int number;
    private String gradeLevel;
    private int kills=0;
    private int attackErrors=0;
    private int totalAttacks=0;
    private int assists=0;
    private int ballErrors=0;
    private int aces=0;
    private int missedServes=0;
    private int serveRecErr=0;
    private int passRecErr=0;
    private int serveAttempts=0;
    private int receptionErrors=0;
    private int receptionAttempts=0;
    private int digs=0;
    private int soloBlock=0;
    private int blockAssists=0;
    private int blockErrors=0;
    private int hitsInPlay=0;
    private int pass1=0;
    private int pass2=0;
    private int pass3=0;
    private double passPercentage = 0;
    private double hittingPercentage =0;
    private String teamName;


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
    //null setter for Libero object
    Player(){

    }

    //setters
    public void setTeamName(String tn){
        teamName = tn;
    }
    public void setHittingPercentage(double h){
        hittingPercentage = h;
    }
    public void setPassPercentage(double p){
        passPercentage=p;
    }
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

    public void setKills(int k){
        kills=k;
    }

    public void setAttackErrors(int e){
        attackErrors=e;
    }

    public void setTotalAttacks(int a){
        totalAttacks = a;
    }

    public void setAssists(int a){
        assists=a;
    }

    public void setBallErrors(int be){
        ballErrors=be;
    }

    public void setAces(int a){
        aces=a;
    }

    public void setServeAttempts(int sa){
        serveAttempts=sa;
    }

    public void setReceptionErrors(int re){
        receptionErrors=re;
    }

    public void setReceptionAttempts(int ra){
        receptionAttempts=ra;
    }

    public void setDigs(int d){
        digs=d;
    }

    public void setSoloBlock(int b){
        soloBlock=b;
    }

    public void setBlockAssists(int ba){
        blockAssists=ba;
    }

    public void setBlockErrors(int be){
        blockErrors=be;
    }

    //increment
    public void incPass1(){
        pass1++;
    }
    public void incPass2(){
        pass2++;
    }
    public void incPass3(){
        pass3++;
    }
    public void incPassRecErr(){
        passRecErr++;
    }
    public void incKills(){
        kills++;
    }

    public void incSerRecErr(){
        serveRecErr++;
    }

    public void incAttackErrors(){
        attackErrors++;
    }

    public void incTotalAttacks(){
        totalAttacks++;
    }

    public void incHitsInPlay(){
        hitsInPlay++;
    }

    public void incAssists(){
        assists++;
    }

    public void incBallErrors(){
        ballErrors++;
    }

    public void incAces(){
        aces++;
    }

    public void incMissedServes(){
        missedServes++;
    }

    public void incServeAttempts(){
        serveAttempts++;
    }

    public void incReceptionErrors(){
        receptionErrors++;
    }

    public void incReceptionAttempts(){
        receptionAttempts++;
    }

    public void incDigs(){
        digs++;
    }

    public void incSoloBlock(){
        soloBlock++;
    }

    public void incBlockAssists(){
        blockAssists++;
    }

    public void incBlockErrors(){
        blockErrors++;
    }

    //getters
    public String getTeamName(){
        return teamName;
    }
    public double getHittingPercentage(){
        return hittingPercentage;
    }
    public int getID() {
        return ID;
    }

    public int getPass1(){
        return pass1;
    }

    public int getPass2(){
        return pass2;
    }

    public int getPass3(){
        return pass3;
    }

    public double getPassPercentage(){
        return passPercentage;
    }
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

    public int getKills(){
        return kills;
    }

    public int getAttackErrors(){
        return attackErrors;
    }

    public int getTotalAttacks(){
        return totalAttacks;
    }

    public int getAssists(){
        return assists;
    }

    public int getBallErrors(){
        return ballErrors;
    }

    public int getAces(){
        return aces;
    }

    public int getMissedServes(){
        return missedServes;
    }

    public int getServeAttempts(){
        return serveAttempts;
    }

    public int getReceptionErrors(){
        return receptionErrors;
    }

    public int getReceptionAttempts(){
        return receptionAttempts;
    }

    public int getDigs(){
        return digs;
    }

    public int getSoloBlock(){
        return soloBlock;
    }

    public int getBlockAssists(){
        return blockAssists;
    }

    public int getBlockErrors(){
        return blockErrors;
    }

    public int getHitsInPlay(){
        return hitsInPlay;
    }

}
