package com.example.myapplication_2;

/*//import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public  class DetailsActivity extends AppCompatActivity {
   public static final int ADD_CODE = 111;
   private int position;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EditText editTextName = findViewById(R.id.edit_book_name);
        Button button_OK = findViewById(R.id.OkButton);
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name",editTextName.getText().toString());
                intent.putExtras(bundle);
                setResult(ADD_CODE,intent);
               // startActivity(intent);
                DetailsActivity.this.finish();
                Toast.makeText(DetailsActivity.this, "您按下OK按钮", Toast.LENGTH_LONG).show();

            }
        });
    }

}
*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailsActivity extends AppCompatActivity {

    public static final int RESULT_CODE_SUCCESS = 666;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        position= this.getIntent().getIntExtra("position",0);
        String title=this.getIntent().getStringExtra("title");
        Double price=this.getIntent().getDoubleExtra("price",0);

        EditText editTextTitle=findViewById(R.id.edit_book_name);

        if(null!=title)
        {
            editTextTitle.setText(title);

        }


        Button buttonOk=findViewById(R.id.OkButton);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("title",editTextTitle.getText().toString());

                bundle.putDouble("price",price);
                bundle.putInt("position",position);

                intent.putExtras(bundle);
                setResult(RESULT_CODE_SUCCESS,intent);
                DetailsActivity.this.finish();
            }
        });
    }
}

