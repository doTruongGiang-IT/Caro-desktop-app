package com.sgu.caro.entity;

import java.util.ArrayList;

public class Group {

    private int user_1;
    private int user_2;
    private ArrayList<Integer> watchers;
    private boolean accept_pairing_1 = false;
    private boolean accept_pairing_2 = false;

    public Group(int user_1, int user_2, ArrayList<Integer> watchers) {
        this.user_1 = user_1;
        this.user_2 = user_2;
        this.watchers = watchers;
    }

    public int getUser_1() {
        return user_1;
    }

    public void setUser_1(int user_1) {
        this.user_1 = user_1;
    }

    public int getUser_2() {
        return user_2;
    }

    public void setUser_2(int user_2) {
        this.user_2 = user_2;
    }

    public ArrayList<Integer> getWatchers() {
        return watchers;
    }

    public void setWatchers(ArrayList<Integer> watchers) {
        this.watchers = watchers;
    }

    public boolean isAccept_pairing_1() {
        return accept_pairing_1;
    }

    public void setAccept_pairing_1(int user_1, boolean accept_pairing_1) {
        if (this.user_1 == user_1) {
            this.accept_pairing_1 = accept_pairing_1;
        }
    }

    public boolean isAccept_pairing_2() {
        return accept_pairing_2;
    }

    public void setAccept_pairing_2(int user_2, boolean accept_pairing_2) {
        if (this.user_2 == user_2) {
            this.accept_pairing_2 = accept_pairing_2;
        }
    }
    
    public int inGroup(int userId){
        if (userId == user_1 || userId == user_2){
            return 1;
        }
        else if (watchers.contains(userId)){
            return 2;
        }
        else{
            return 0;
        }
    }
    
    public String toString(){
        return String.valueOf(Math.min(user_1, user_2)) + "|" + String.valueOf(Math.max(user_1, user_2));
    }
}