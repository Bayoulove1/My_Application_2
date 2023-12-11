package com.jnu.student;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class BookDetailsActivity extends AppCompatActivity {
    private EditText newDataEditText;
    private EditText newPriceEditText;
    private Book book;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        newDataEditText = findViewById(R.id.edit_text_new_data);
        newPriceEditText = findViewById(R.id.editTextNumber2);
        book = (Book) getIntent().getSerializableExtra("bookToEdit");
        position = getIntent().getIntExtra("position", 0);
        newDataEditText.setText(book.getTitle());
        newPriceEditText.setText(book.getPrice());
    }

    public void onSaveButtonClick(View view) {
        String newData = newDataEditText.getText().toString();
        int newPrice = Integer.parseInt(newDataEditText.getText().toString());
        if (!newData.isEmpty()) {
            Intent resultIntent = new Intent();
            book.setTitle(newData);
            book.setPrice(newPrice);
            resultIntent.putExtra("book", book);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            // 处理数据为空的情况，可以显示一个提示或错误消息
            newDataEditText.setError("数据不能为空");
        }
    }
}
