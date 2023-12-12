package com.jnu.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<Book> books;
    //private static final int DEFAULT_COVER_RESOURCE_ID = R.drawable.book_no_name; // 默认封面图片的资源ID

    public BookListAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.price.setText(Integer.toString(book.getPrice()));
        holder.itemView.setOnLongClickListener(v -> {
            // 处理长按事件，显示上下文菜单或其他操作
            v.showContextMenu();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void addData(String newData,int newPrice) {
        books.add(new Book(newData, newPrice));
        notifyItemInserted(books.size() - 1);
    }

    public void deleteData(int position) {
        if (position >= 0 && position < books.size()) {
            books.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Book getBookAtPosition(int position) {
        return books.get(position);
    }

    public void updateBook(Book book,int position){
        Book bookAtPosition = getBookAtPosition(position);
        bookAtPosition.setTitle(book.getTitle());
        bookAtPosition.setPrice(book.getPrice());
        notifyItemChanged(position);
    }
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;
        TextView price;
        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.text_view_tasks_title);
            price = itemView.findViewById(R.id.text_view_price);
            CheckBox checkBox = itemView.findViewById(R.id.checkBox);
//            checkBox.setOnCheckedChangeListener(new View.OnClickListener(){
//                if(isChecked){
//
//                }
//            });
        }
    }
}
