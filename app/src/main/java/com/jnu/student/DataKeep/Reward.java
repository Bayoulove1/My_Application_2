package com.jnu.student.DataKeep;

import java.io.Serializable;

public class Reward implements Serializable {
    private String Name;
    private double Score;
    private boolean select;
    public String getName(){
        return Name;
    }
    public double getScore(){
        return Score;
    }
    public Reward(String name_, double Score_){
        this.Name = name_;
        this.Score = Score_;
    }
    public void setName(String name){
        this.Name = name;
    }
    public void setScore(double score){
        this.Score = score;
    }
    public  void setSelect(boolean Select){
        select = Select;
    }
    public boolean Select(){
        return select;
    }
}
