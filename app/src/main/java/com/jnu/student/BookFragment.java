package com.jnu.student;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class BookFragment extends Fragment {

    private static final int EDIT_DATA_REQUEST_CODE = 2;
    private BookListAdapter adapter;
    private static final int ADD_DATA_REQUEST_CODE = 1;
    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (data != null) {
                String newData = data.getStringExtra("newData");
                adapter.addData(newData);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookListAdapter(bookList);
        recyclerView.setAdapter(adapter);

        // 关联上下文菜单
        registerForContextMenu(recyclerView);
        recyclerView.setOnCreateContextMenuListener(this); // 设置上下文菜单的创建监听器
        return view;
    }


    private List<Book> getListBooks() {
        try {
            File file = new File(requireActivity().getFilesDir(), "Serializable.txt");
            if (!file.exists()) {
                file.createNewFile();
                books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
                books.add(new Book("创新工程实践", R.drawable.book_no_name));
                books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
                saveListBooks();
                return  books;
            }
            ObjectInputStream inputStream = new ObjectInputStream(requireActivity().openFileInput("Serializable.txt"));
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
            ObjectOutputStream outputStream = new ObjectOutputStream(requireActivity().openFileOutput("Serializable.txt", MODE_PRIVATE));
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
            books.remove(position);
            adapter.notifyItemRemoved(position);
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