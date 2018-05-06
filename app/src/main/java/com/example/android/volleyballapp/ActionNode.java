package com.example.android.volleyballapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dave on 5/2/2018.
 */

//This class will hold all the information for an action within a match.
//it will be used within a ActionStack to determine the order of events in a game.
public class ActionNode {
    private List<Player> playersOnCourtList;
    private List<Player> playersOnBenchList;
    private Player libPlayer;
    private int myTeamScore;
    private int otherTeamScore;
    private int myTeamGames;
    private int otherTeamGames;
    private boolean serveIndicator;
    private int mySubsUsed;
    private int rotation;
    private int myTO;
    private int otherTeamTO;
    private String action;
    private Player activePlayer;
    private Player subbedForPlayer;
    private Player server;
    private boolean serveAttempt;
    private ActionNode below;
    ActionNode(List<Player> poc,List<Player> pob, Player l, int mts, int ots, int mtg, int otg,
               boolean s, int msu, int r, int mto, int oto, String a, Player ap, Player sfp,Player ser,Boolean sa){
        playersOnCourtList = poc;
        playersOnBenchList = pob;
        libPlayer = l;
        myTeamScore = mts;
        otherTeamScore = ots;
        myTeamGames = mtg;
        otherTeamGames = otg;
        serveIndicator = s;
        mySubsUsed = msu;
        rotation = r;
        myTO = mto;
        otherTeamTO = oto;
        action = a;
        activePlayer = ap;
        subbedForPlayer = sfp;
        server = ser;
        serveAttempt=sa;
    }
    public void setBelow(ActionNode b){
        below = b;
    }
    public ActionNode getBelow(){
        return below;
    }
    public List<Player> getPlayersOnCourtList(){
        return  playersOnCourtList;
    }
    public List<Player> getPlayersOnBenchList(){
        return  playersOnBenchList;
    }
    public Player getLibPlayer(){
        return libPlayer;
    }
    public int getMyTeamScore(){
        return myTeamScore;
    }
    public int getOtherTeamScore(){
        return otherTeamScore;
    }
    public int getMyTeamGames(){
        return myTeamGames;
    }
    public int getOtherTeamGames(){
        return otherTeamGames;
    }
    public boolean getServeIndicator(){
        return serveIndicator;
    }
    public int getMySubsUsed(){
        return mySubsUsed;
    }
    public int getRotation(){
        return rotation;
    }
    public int getMyTO(){return myTO;}
    public int getOtherTeamTO(){return otherTeamTO;}
    public String getAction(){
        return action;
    }
    public Player getActivePlayer(){
        return activePlayer;
    }
    public Player getSubbedForPlayer(){
        return subbedForPlayer;
    }
    public Player getServer(){
        return server;
    }
    public Boolean getServeAttempt(){
        return serveAttempt;
    }
}
