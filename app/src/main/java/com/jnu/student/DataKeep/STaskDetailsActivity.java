package com.jnu.student.DataKeep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jnu.student.R;

public class STaskDetailsActivity extends AppCompatActivity {
    private int position=-1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        Intent intent=getIntent();
        if(intent!=null){
            //从Intent中获取传递的数据
            String name=intent.getStringExtra("name");

            if(null!=name){
                double score = intent.getDoubleExtra("score", 0); // 默认值为0
                position =intent.getIntExtra("position",-1);

                EditText editTextItemName=findViewById(R.id.editTextItemName);
                editTextItemName.setText(name);
                EditText editTextItemScore=findViewById(R.id.editTextItemScore);
                editTextItemScore.setText(Double.toString(score));
            }
        }

        Button button=findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                EditText editTextItemName=findViewById(R.id.editTextItemName);
                EditText editTextItemScore=findViewById(R.id.editTextItemScore);
                intent.putExtra("name",editTextItemName.getText().toString());
                intent.putExtra("score", editTextItemScore.getText().toString());
                intent.putExtra("position",position);
                setResult(Activity.RESULT_OK,intent);
                STaskDetailsActivity.this.finish();
            }
        });
        Button reButton = findViewById(R.id.button_return);
        reButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STaskDetailsActivity.this.finish();
            }
        });
    }

}
