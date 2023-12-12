package com.jnu.student;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<Book> books;
    private AdapterView.OnItemClickListener listener;
    //private static final int DEFAULT_COVER_RESOURCE_ID = R.drawable.book_no_name; // 默认封面图片的资源ID
    public static int score = 0;
    public BookListAdapter(List<Book> books) {
        this.books = books;
    }
    public void setListener(AdapterView.OnItemClickListener listener){
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(int data);
    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }
    public static BookFragment newInstance(int Score){
        Bundle bundle = new Bundle();
        bundle.putInt("Score",score);
        BookFragment fragment = new BookFragment();
        fragment.setArguments(bundle);
        return fragment;
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
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                //listener.onItemClick(17);
//            }
//        });

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
        private TextView textViewScore;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.text_view_tasks_title);
            price = itemView.findViewById(R.id.text_view_price);
            CheckBox checkBox = itemView.findViewById(R.id.checkBox);
//            //itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    score = 12;

                //public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // 在这里处理 CheckBox 被点击时的逻辑

                      // TextView scoreTextView = getTextViewScore();

                        //Integer.parseInt(scoreTextView.getText().toString());
//                       //checkBox被选中
                            checkBox.setChecked(false);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("score", score);

                        // 可以执行其他操作，例如修改数据等
                    }

            }
            );
        }
//               // public TextView getTextViewTitle() {
//                    return textViewTitle;
//                }

                public TextView getTextViewScore() {
                   return price;
               }
    }
}


