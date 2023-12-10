package com.jnu.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public class AddDataActivity extends AppCompatActivity {
    private EditText newDataEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        newDataEditText = findViewById(R.id.edit_text_new_data);
    }

    public void onSaveButtonClick(View view) {
        String newData = newDataEditText.getText().toString();

        if (!newData.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newData", newData);

            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            // 处理数据为空的情况，可以显示一个提示或错误消息
            newDataEditText.setError("数据不能为空");
        }
    }
}
