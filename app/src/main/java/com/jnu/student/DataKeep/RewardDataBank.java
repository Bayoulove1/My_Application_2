package com.jnu.student.DataKeep;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RewardDataBank {
    final String Data_FILENAME21="Reward.data";
    final String SCORE_FILENAME21 = "RewardScore.data";
    public ArrayList<Reward> AddReward(Context applicationContext) {
        ArrayList<Reward> data = new ArrayList<>();
        try {
            FileInputStream fis = applicationContext.openFileInput(Data_FILENAME21);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<Reward>) ois.readObject();
            ois.close();
            fis.close();
            Log.d("Serialization1", "Data loaded successfully.item count" + data.size() );
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }


    public void SaveReward(Context context, ArrayList<Reward> dayRewardItems) {
        try {
            FileOutputStream fos = context.openFileOutput(Data_FILENAME21, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dayRewardItems);
            oos.close();
            fos.close();
            Log.d("Serialization1", "Data serialized and saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SaveTotalScore(Context context, double totalScore) {
        try {
            FileOutputStream fos = context.openFileOutput(SCORE_FILENAME21, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeDouble(totalScore);
            oos.close();
            fos.close();
            Log.d("Serialization1", "Total score saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double AddTotalScore(Context applicationContext) {
        double totalScore = 0.0;
        try {
            FileInputStream fis = applicationContext.openFileInput(SCORE_FILENAME21);
            ObjectInputStream ois = new ObjectInputStream(fis);
            totalScore = ois.readDouble();
            ois.close();
            fis.close();
            Log.d("Serialization1", "Total score loaded successfully.");
        } catch (IOException e) {
            Log.d("Serialization1", "No previous score found, setting to zero.");
            // 这里的日志信息表明没有找到之前保存的分数，因此将其设为零
        }
        return totalScore;
    }
}
