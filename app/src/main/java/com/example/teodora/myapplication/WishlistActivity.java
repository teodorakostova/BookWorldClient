package com.example.teodora.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.teodora.myapplication.database.BookManager;
import com.example.teodora.myapplication.utils.Book;
import com.example.teodora.myapplication.utils.BooksHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    private ArrayList<String> wishlistBooks = new ArrayList<String>();

    private HashSet<String> alreadyReadBooks = new HashSet<>();

    public static final String ALREADY_READ_BOOKS = "ALREADY_READ_BOOKS";

    public static final String ACTIVITY_TITLE = "Reading wishlist";

    private BookManager dbhelper;

    private EditText wishlistEditBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(ACTIVITY_TITLE);
        dbhelper = new BookManager(this);
        dbhelper.truncate();
        wishlistEditBook = (EditText) findViewById(R.id.wishEdit);
        if (BooksHolder.getWishlistBooks().size() > 0) {
            wishlistBooks.addAll(BooksHolder.getWishlistBooks().keySet());
        } else {
            List<Book> books = dbhelper.getAllWishlistBooks();
            for (Book b : books) {
                wishlistBooks.add(b.getTitle());
            }
        }



        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wishlistBooks);
        ListView wishlistView = (ListView) findViewById(R.id.wishlist_view);
        wishlistView.setAdapter(adapter);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), AlreadyReadActivity.class);
                String selectedBookTitle = wishlistBooks.get(position);
                alreadyReadBooks.add(selectedBookTitle);
                intent.putStringArrayListExtra(ALREADY_READ_BOOKS, wishlistBooks);
                Book newBook = new Book();
                newBook.setTitle(selectedBookTitle);
                newBook.setDateRead(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                newBook.setStatus(1);
                BooksHolder.addAlreadyReadBook(newBook);
                BooksHolder.removeWishlistBook(newBook.getTitle());
                wishlistBooks.remove(position);
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }
        };

        wishlistView.setOnItemClickListener(onItemClickListener);
        final AlertDialog.Builder invalidTitle  = new AlertDialog.Builder(this);
        wishlistEditBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!validate(s.toString())) {
                    displayError();
                }
            }
        });

    }

    private void displayError() {
        AlertDialog.Builder invalidTitle  = new AlertDialog.Builder(this);
        invalidTitle.setMessage("Book title cannot contain quotes.");
        invalidTitle.setTitle("Oops");
        invalidTitle.setPositiveButton("OK", null);
        invalidTitle.setCancelable(true);
        invalidTitle.create().show();

        invalidTitle.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    private boolean validate(String text) {
        return !text.matches(".*[\'|\"|\\|\r|\n].*");
    }
    /**
     * Called when the user taps the Send button
     */
    public void addWishToRead(View view) {
        String title = wishlistEditBook.getText().toString();
        if (validate(title)) {
            adapter.add(title);
            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setDateRead(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            newBook.setStatus(0);
            BooksHolder.addWishlistBook(newBook);
        } else {
            displayError();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Map<String, Book> toBeInserted = BooksHolder.getAlreadyReadBooks();
        toBeInserted.putAll(BooksHolder.getWishlistBooks());
        dbhelper.insertBooks(toBeInserted);
    }
}
