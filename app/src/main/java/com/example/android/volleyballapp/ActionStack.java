package com.example.android.volleyballapp;


//this class is used to create the ActionStack and provide it with push pop and peek methods.
//It uses the ActionNode class
public class ActionStack {
    private ActionNode top;
    ActionStack(){

    }
    public void push(ActionNode a){
        a.setBelow(top);
        top = a;
    }
    public ActionNode pop(){
        ActionNode temp = top;
        top = top.getBelow();
        return temp;
    }
    public ActionNode peek(){
        return top;
    }
}
