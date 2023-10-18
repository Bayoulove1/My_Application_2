package com.example.myapplication_2;
//第二次实验 activity_main.xml 和 shop_item_list.xml
//RecyclerView添加长按菜单

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    MyAdapter mMyAdapter;
    List<News> mNewsList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.re_view);
        //String []titles = new String[]{"《软件项目管理案例教程》（第四版）","《创新工程实践》","《信息安全数学基础》（第二版）"};
        mNewsList.add(new News("《软件项目管理案例教程》（第四版）", R.drawable.book_1));
        mNewsList.add(new News("《创新工程实践》", R.drawable.book_2));
        mNewsList.add(new News("《信息安全数学基础》（第二版）", R.drawable.book_no_name));
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        //1、注册长按菜单(onCreate)
        registerForContextMenu(mRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }
    //2、显示上下文菜单项
    public void onCreateContextMenu(ContextMenu menu, View v,

                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("具体操作：");
        menu.add(0, 0, Menu.NONE, "添加");
        menu.add(0, 1, Menu.NONE, "删除");
        menu.add(0, 2, Menu.NONE, "修改");
    }
    //3、点击菜单，有响应效果
    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case 0:
                Toast.makeText(MainActivity.this, "您选中的是编辑操作", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(MainActivity.this, "您选中的是删除操作", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(MainActivity.this, "您选中的是修改操作", Toast.LENGTH_LONG).show();
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.shop_item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mImageTv.setImageResource(news.picture);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView mTitleTv;
        ImageView mImageTv;
        TextView mTitleContent;

        public MyViewHoder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView1);
            mImageTv = itemView.findViewById(R.id.imageView);
            //4、添加菜单的视类监听响应
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }
}


//第一次实验代码：activity_main_1.xml
//创建layout布局，添加控件等
/*public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    View Button_one; //创建一个button
    View Button_two;
    View Button_three;
    View Button_four;//因为第四个按钮是在xml文件中直接添加响应函数的，所以在这里不声明对象也是可以的。

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);

        Button_one = findViewById(R.id.button1); //通过id找到对应button
        Button_one.setOnClickListener(new MyButtonClickListener());
        Toast.makeText(MainActivity.this,"message",Toast.LENGTH_LONG).show();

        Button_two = findViewById(R.id.button2);
        Button_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//在这里就是重写了
                TextView clickedbutton2 = (TextView) view;
                clickedbutton2.setText("重写onClick函数！");
                showDialog(MainActivity.this,"hints","打开对话框");
            }
        });

        Button_three = findViewById(R.id.button3);
        Button_three.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        TextView clickedbutton3 = (TextView) view;

        clickedbutton3.setText("this设置监听器！");

    }

    public void XMLonClick(View view) {
        TextView clickedbutten4 = (TextView) view;
        clickedbutten4.setText("XML文件添加响应函数！");
    }


    private class MyButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            TextView clickedbutton1 = (TextView) view;
            clickedbutton1.setText("私有类重写onClick函数！");

        }
    }
    public void showDialog(Context context, String title, String message){
        //context表示当前环境
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //创建一个大的对话框
        builder.setTitle(title).setMessage(message).setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //用户点击确定按钮
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
*/

