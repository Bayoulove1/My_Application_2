package com.example.myapplication_2;
/* 任务界面显示功能：
1、通过Fragment可以实现多种类型的任务，包括 天计划，周计划，年计划(先实现日计划)
2、通过弹出上下文菜单实现对任务的添加、修改和删除（非常好解决的，基本不会有太大的问题）（功能基本完成）
3、项目完成之后要增加金币（设置全局变量，完成之后增加值就行（我猜的。先这样写着，后续有其他想法再说））*/
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Task extends Fragment {
    RecyclerView mRecyclerView;

    private Task.MyAdapter mMyAdapter;

    private ArrayList<dataList> mNewsList;
    private ActivityResultLauncher<Intent> addDataLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            ,result -> {
                if(null!=result){
                    Intent intent=result.getData();
                    if(result.getResultCode()==DetailsActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle=intent.getExtras();
                        String title= bundle.getString("title");
                        int position = bundle.getInt("position");
                        mNewsList.add(position, new dataList(title,R.drawable.ic_launcher_background) );
                        new DataSaver().Save(this.getContext(),mNewsList);
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

    public void Task() {
        // Required empty public constructor
    }

    public static Task newInstance() {
        Task fragment = new Task();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }
    //ActivityResultLauncher<Intent> addDataLauncher;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_book_item, container, false);
        RecyclerView recyclerViewMain=rootView.findViewById(R.id.recycleview_main);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMain.setLayoutManager(linearLayoutManager);

        DataSaver dataSaver=new DataSaver();
        mNewsList=dataSaver.Load(this.getContext());
        mMyAdapter= new MyAdapter(mNewsList);
        recyclerViewMain.setAdapter(mMyAdapter);
        return rootView;
    }
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent=new Intent(this.getContext(), DetailsActivity.class);
                intent.putExtra("position",item.getOrder());
                addDataLauncher.launch(intent);
                Toast.makeText(Task.this.getContext(), "您选中的是添加操作", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(this.getContext(),"item delete " +item.getOrder()+" clicked!",Toast.LENGTH_LONG)
                        .show();
                //添加询问是否删除的对话框
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle("Delete data")
                        .setMessage("Are you want to delete this data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mNewsList.remove(item.getOrder());
                                //new DataSaver().Save(BookItemFragment.this.getContext(),mNewsList);
                                mMyAdapter.notifyItemRemoved(item.getOrder());
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
                Toast.makeText(Task.this.getContext(), "您选中的是删除操作", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Intent intentUpdate=new Intent(this.getContext(), DetailsActivity.class);
                intentUpdate.putExtra("position",item.getOrder());
                intentUpdate.putExtra("title",mNewsList.get(item.getOrder()).getTitle());
                updateDataLauncher.launch(intentUpdate);
                Toast.makeText(Task.this.getContext(), "您选中的是修改操作", Toast.LENGTH_LONG).show();
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHoder> {
        private ArrayList<dataList> localDataSet;
        public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView mTitleTv;
            ImageView mImageTv;
            TextView mTitleContent;
            TextView textViewTitle;
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

            public TextView getTextViewTitle() {
                return textViewTitle;
            }
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,getAdapterPosition(),"Add "+getAdapterPosition());
                contextMenu.add(0,1,getAdapterPosition(),"Delete "+getAdapterPosition());
                contextMenu.add(0,2,getAdapterPosition(),"Update "+getAdapterPosition());
            }
        }
        public MyAdapter(ArrayList<dataList> dataSet){
            localDataSet = dataSet;
        }
        public MyViewHoder onCreateViewHolder(ViewGroup viewGroup,int viewType){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_details,viewGroup,false);
            return new MyViewHoder(view);
        }
        public void onBindViewHolder(MyViewHoder myViewHoder,final int position){
            myViewHoder.getTextViewTitle().setText(localDataSet.get(position).getTitle());
            myViewHoder.getImageViewImage().setImageResource(localDataSet.get(position).getResourceId());
        }
        public int getItemCount(){
            return localDataSet.size();
        }
    }
}




