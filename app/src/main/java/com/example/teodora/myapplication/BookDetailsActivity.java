package com.example.teodora.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teodora.myapplication.database.BookManager;
import com.example.teodora.myapplication.utils.Book;
import com.example.teodora.myapplication.webservices.BookRetrieveTask;
import com.example.teodora.myapplication.utils.BooksHolder;
import com.example.teodora.myapplication.webservices.BooksRetrieveTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class BookDetailsActivity extends AppCompatActivity {

    private List<Book> booksByAuthor = new ArrayList<>();

    private ArrayList<String> recommended = new ArrayList<>();

    private Book selectedRecommended = new Book();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        setTitle("More by this author");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String selectedBookTitle = sharedPref.getString(
                getString(R.string.already_read_book), "");

        try {

            selectedBookTitle = URLEncoder.encode(selectedBookTitle, "UTF-8");
            Book bookWithTitle = new BookRetrieveTask().
                    execute("http://openlibrary.org/search.json?title=" + selectedBookTitle).get();
            String author = bookWithTitle.getAuthor();
            author = URLEncoder.encode(author, "UTF-8");
            booksByAuthor = new BooksRetrieveTask().
                    execute("http://openlibrary.org/search.json?author=" + author).get();
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int i = 0;
        for (Book book : booksByAuthor) {
            if (i > 30) {
                break;
            }
            if (!selectedBookTitle.equals(book.getTitle())) {
                recommended.add(book.getTitle());
            }
            i++;
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recommended);
        final ListView wishlistView = (ListView) findViewById(R.id.by_this_author);
        wishlistView.setAdapter(adapter);


        /* Removes element from the list on click */
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), AlreadyReadActivity.class);
                Book selectedBook = booksByAuthor.get(position);
                selectedBook.setStatus(0);
                BooksHolder.addWishlistBook(selectedBook);
                selectedRecommended = selectedBook;
                recommended.remove(position);
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }
        };

        wishlistView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new BookManager(this).insertBook(selectedRecommended);
    }

}
