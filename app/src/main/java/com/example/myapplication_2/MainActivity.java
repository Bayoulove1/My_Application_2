package com.example.myapplication_2;
//第二次实验 activity_main.xml 和 shop_item_list.xml
//平行滑动

//谷歌搜索接口

//地图接口加载
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
//Tablayout+ViewPager2+Fragment
public class MainActivity extends AppCompatActivity {
    public class PageViewFragmentAdapter extends FragmentStateAdapter {
        public PageViewFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @Override
        public Fragment createFragment(int position) {
            switch(position)
            {
                case 0:
                    return BookItemFragment.newInstance();
                case 2:
                    return BrowserFragment.newInstance();
            }
            return BookItemFragment.newInstance();
        }

        @Override

        public int getItemCount() {
            return 3;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2Main= findViewById(R.id.viewpager2_main);
        viewPager2Main.setAdapter(new PageViewFragmentAdapter(getSupportFragmentManager(),getLifecycle()));
        TabLayout tabLayout=findViewById(R.id.tablayout_header);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2Main, new TabLayoutMediator.TabConfigurationStrategy() {
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                        tab.setText("Book item");
                        break;
                    case 1:
                        tab.setText("Browser");
                        break;
                    case 2:
                        tab.setText("Map");
                        break;
                }
            }

    });
        tabLayoutMediator.attach();
}
}
/*public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    public MyAdapter mMyAdapter;
    private Button Add_submit;
    List<News> mNewsList = new ArrayList<>();
    //private MainRecycleViewAdapter mainRecycleViewAdapter;

    private ActivityResultLauncher<Intent> addDataLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            ,result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==DetailsActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle=intent.getExtras();
                        String title= bundle.getString("title");
                        int position = bundle.getInt("position");

                        mNewsList.add(position, new News(title,R.drawable.ic_launcher_background) );
                        mMyAdapter.notifyItemInserted(position);
                    }
                }
            });
    private ActivityResultLauncher<Intent> updateDataLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            ,result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==DetailsActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle=intent.getExtras();
                        String title= bundle.getString("title");

                        int position=bundle.getInt("position");
                        mNewsList.get(position).setTitle(title);
                        mMyAdapter.notifyItemChanged(position);
                    }
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context mContext = this;
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.re_view);
        //String []titles = new String[]{"《软件项目管理案例教程》（第四版）","《创新工程实践》","《信息安全数学基础》（第二版）"};
        mNewsList.add(new News("《软件项目管理案例教程》（第四版）", R.drawable.book_1));
        mNewsList.add(new News("《创新工程实践》", R.drawable.book_2));
        mNewsList.add(new News("《信息安全数学基础》（第二版）", R.drawable.book_no_name));
        mMyAdapter = new MyAdapter();

        registerForContextMenu(mRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMyAdapter);
    }
    //ActivityResultLauncher<Intent> addDataLauncher;

    public boolean onContextItemSelected(MenuItem item) {
        // 得到当前被选中的item信息
       // AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //Toast.makeText(this,"clicked"+item.getOrder(),Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case 0:
                Intent intent=new Intent(this, DetailsActivity.class);
                intent.putExtra("position",item.getOrder());
                addDataLauncher.launch(intent);
                Toast.makeText(MainActivity.this, "您选中的是添加操作", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(this,"item delete " +item.getOrder()+" clicked!",Toast.LENGTH_LONG)
                        .show();
                //添加询问是否删除的对话框
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Delete data")
                        .setMessage("Are you want to delete this data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mNewsList.remove(item.getOrder());
                                new DataSaver().Save(MainActivity.this,mNewsList);
                                mMyAdapter.notifyItemRemoved(item.getOrder());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
                Toast.makeText(MainActivity.this, "您选中的是删除操作", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Intent intentUpdate=new Intent(this, DetailsActivity.class);
                intentUpdate.putExtra("position",item.getOrder());
                intentUpdate.putExtra("title",mNewsList.get(item.getOrder()).getTitle());
                updateDataLauncher.launch(intentUpdate);
                Toast.makeText(MainActivity.this, "您选中的是修改操作", Toast.LENGTH_LONG).show();
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
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

        public TextView getTextView() {
            return mTitleTv;
        }
            public ImageView getImageViewImage() {
                return mImageTv;
            }
        public MyViewHoder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView1);
            mImageTv = itemView.findViewById(R.id.imageView);
            //4、添加菜单的视类监听响应
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,getAdapterPosition(),"Add "+getAdapterPosition());
                contextMenu.add(0,1,getAdapterPosition(),"Delete "+getAdapterPosition());
                contextMenu.add(0,2,getAdapterPosition(),"Update "+getAdapterPosition());
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

