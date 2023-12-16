package com.jnu.student.DataKeep;

import java.io.Serializable;

public class ShortTask implements Serializable {
    public String getTitle() {
        return Title;
    }

    public double getScore() {
        return Score;
    }

    private String Title;         //名字
    private double Score;            //分数
    private boolean isSelected; // 复选框的选中状态
    public ShortTask(String Title_, double score_) {
        this.Title=Title_;
        this.Score = score_;
    }


    public void setTitle(String title) {
        this.Title = title;
    }

    public void setScore(double score) {this.Score = score;}
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
