package com.jnu.student.DataKeep;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GainScoreDataBank {
    final String ACHIEVEMENT_FILENAME = "wallet.data";

    public synchronized  void WalletSave(Context context, double points) {
        try {
            FileOutputStream fos = context.openFileOutput(ACHIEVEMENT_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeDouble(points);
            oos.close();
            fos.close();
            Log.d("Serialization", "All Score serialized and saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized  double WalletLoad(Context applicationContext) {
        double points = 0.0;
        try {
            FileInputStream fis = applicationContext.openFileInput(ACHIEVEMENT_FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            points = ois.readDouble();
            ois.close();
            fis.close();
            Log.d("Serialization", "All score loaded successfully.");
        } catch (IOException e) {
            Log.d("Serialization", "No previous score found, setting to zero.");
        }
        return points;
    }

}
