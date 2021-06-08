package com.example.googlebooksappclone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    ArrayList<BookItem> BooksListItem = new ArrayList<>();
    BookClickListener mBookClickListener;

    public BooksAdapter(BookClickListener bookClickListener){
        this.mBookClickListener = bookClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(v -> mBookClickListener.onBookItemClickListener(BooksListItem.get(viewHolder.getAdapterPosition())));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        BookItem currentBook = BooksListItem.get(position);
        String title = currentBook.getTitle();
        String author = currentBook.getAuthor();
        String description = currentBook.getDescription();
        String ImageUrl = currentBook.getImageUrl();
        holder.getTitle().setText(title);
        holder.getDescription().setText(description);
        holder.getAuthor().setText(author);
        Glide.with(holder.itemView.getContext()).load(ImageUrl).into(holder.getBookCover());
    }

    @Override
    public int getItemCount() {
        return BooksListItem.size();
    }

    public void updateBooks(ArrayList<BookItem> updatedBooks){
        BooksListItem.clear();
        BooksListItem.addAll(updatedBooks);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView author;
        TextView description;
        ImageView bookCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authors);
            description = itemView.findViewById(R.id.description);
            bookCover = itemView.findViewById(R.id.bookCover);
        }

        /* Return the title Textview */
        public TextView getTitle(){ return title; }

        /* Return the author Textview */
        public TextView getAuthor(){ return author; }

        /* Return the description Textview */
        public TextView getDescription(){ return description; }

        /* Return the book cover Textview */
        public ImageView getBookCover(){ return bookCover; }
    }
}

interface BookClickListener{
    void onBookItemClickListener(BookItem book);
}