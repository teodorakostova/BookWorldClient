package com.example.teodora.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.teodora.myapplication.database.BookManager;
import com.example.teodora.myapplication.utils.Book;
import com.example.teodora.myapplication.utils.BooksHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlreadyReadActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_read);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String, Book> alreadyReadBooks = BooksHolder.getAlreadyReadBooks();
        List<Book> allAlreadyReadBookDb = new BookManager(this).getAllReadBooks();
        allAlreadyReadBookDb.addAll(alreadyReadBooks.values());

        final Set<String> titles = new HashSet<>();
        final Map<String, Book> alreadyReadBooksTitle = new HashMap<>();
        for (Book book : allAlreadyReadBookDb) {
            if (book.getStatus() == 1) {
                titles.add(book.getTitle());
                alreadyReadBooksTitle.put(book.getTitle(), book);
            }

        }
        final List<String> titlesLst = new ArrayList<>();
        for (String title : titles) {
            titlesLst.add(title);
        }

        ListView alreadyReadView = (ListView) findViewById(R.id.already_read_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titlesLst);
        alreadyReadView.setAdapter(adapter);
        //final ;

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), BookDetailsActivity.class);
                String selectedBookTitle = titlesLst.get(position);

                Book selectedBook = alreadyReadBooksTitle.get(selectedBookTitle);
                Log.d("TEDI ", selectedBookTitle);
                BooksHolder.addAlreadyReadBook(selectedBook);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.already_read_book), selectedBookTitle);
                editor.apply();
                startActivity(intent);
            }
        };

        alreadyReadView.setOnItemClickListener(onItemClickListener);

    }
}
