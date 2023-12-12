package com.jnu.student;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class BookFragment extends Fragment {
    private static int score;
    private static final int EDIT_DATA_REQUEST_CODE = 2;
    private BookListAdapter adapter;
    private static final int ADD_DATA_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (data != null) {
                String newData = data.getStringExtra("newData");
                int newPrice = data.getIntExtra("newPrice",000);
                adapter.addData(newData,newPrice);
                saveListBooks();
            }

        }
    });

    ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (data != null) {
                Book newData = (Book) data.getSerializableExtra("book");
                int position = data.getIntExtra("position", 0);
                adapter.updateBook(newData, position);
                saveListBooks();
            }

        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,container,false);
        List<Book> bookList = getListBooks();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_books);
        TextView textView = view.findViewById(R.id.textView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookListAdapter(bookList);
        recyclerView.setAdapter(adapter);
        getParentFragmentManager().setFragmentResultListener("upDateScore",this,(requestKey, result) -> {
            score = new Score().loadScore(this.getContext());
            int updateScore = 0;
            updateScore = updateScore + score;
            textView.setText(String.valueOf(updateScore));
            new Score().saveScore(this.getContext(),updateScore);
            if (getActivity() != null) {
                int all_score = new Score().loadScore(this.getContext());
                Bundle bundle = new Bundle();
                bundle.putInt("allScore", all_score);
                getParentFragmentManager().setFragmentResult("AllScore", bundle);
            }
        });
        Button buttonZero = view.findViewById(R.id.button_zero);
        // 设置按钮点击事件监听器
        buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在此处执行按钮点击后的操作
                Toast.makeText(getContext(), "All Score 归零", Toast.LENGTH_SHORT).show();
                textView.setText(String.valueOf(0));
                new Score().saveScore(getContext(),0);
            }
        });
        // 关联上下文菜单
        registerForContextMenu(recyclerView);
        recyclerView.setOnCreateContextMenuListener(this); // 设置上下文菜单的创建监听器
        return view;
    }


    private List<Book> getListBooks() {
        try {
            File file = new File(requireActivity().getFilesDir(), "\"C:\\Users\\bayoulove1\\Desktop\\out.txt\"");
            if (!file.exists()) {
                file.createNewFile();
                books.add(new Book("长按开始一项计划吧！",1314));
                saveListBooks();
                return  books;
            }
            ObjectInputStream inputStream = new ObjectInputStream(requireActivity().openFileInput("\"C:\\Users\\bayoulove1\\Desktop\\out.txt\""));
            Object object = inputStream.readObject();
            if (object != null) {
                books.addAll((Collection<? extends Book>) object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void saveListBooks() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(requireActivity().openFileOutput("\"C:\\Users\\bayoulove1\\Desktop\\out.txt\"", MODE_PRIVATE));
            outputStream.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 启动另一个Activity以添加数据
    private void startAddActivity() {
        Intent intent = new Intent(requireActivity(), AddDataActivity.class);
        addLauncher.launch(intent);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.recycler_view_books) {
            MenuInflater inflater = requireActivity().getMenuInflater();
            inflater.inflate(R.menu.main_context_menu, menu);
        }
    }


    @SuppressLint("NonConstantResourceId")
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // 检查 info 是否为空
        if (info == null) {
            return super.onContextItemSelected(item);
        }

        int position = info.position;
        int menuItemId = item.getItemId(); // 获取菜单项的 ID

        if (menuItemId == R.id.menu_add) {
            startAddActivity();
            return true;
        } else if (menuItemId == R.id.menu_edit) {
            editDataAtPosition(position);
            return true;
        } else if (menuItemId == R.id.menu_delete) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this.getContext())
                    .setMessage("确定要删除这项任务吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            books.remove(item.getOrder());
                            saveListBooks();
                            adapter.notifyItemRemoved(item.getOrder());
                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create();
            alertDialog.show();
//            books.remove(position);
//            adapter.notifyItemRemoved(position);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }


    // 修改数据，可以根据位置position修改数据
    private void editDataAtPosition(int position) {
        // 处理修改数据的逻辑，可以打开一个新Activity进行编辑
        // 这里您需要启动一个新的Activity来编辑特定位置的数据
        // 例如：您可以通过传递数据和位置来编辑该数据

        Book bookToEdit = adapter.getBookAtPosition(position);
        Intent intent = new Intent(requireActivity(), BookDetailsActivity.class);
        intent.putExtra("bookToEdit", bookToEdit);
        intent.putExtra("position", position);
        editLauncher.launch(intent);
    }

    // 删除数据，可以根据位置position删除数据
    private void deleteDataAtPosition(int position) {
        adapter.deleteData(position);
    }

    List<Book> books = new ArrayList<>();
}