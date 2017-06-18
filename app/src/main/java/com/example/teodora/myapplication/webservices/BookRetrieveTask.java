package com.example.teodora.myapplication.webservices;

import android.os.AsyncTask;

import com.example.teodora.myapplication.utils.Book;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Teodora on 6/18/2017.
 */

public class BookRetrieveTask extends AsyncTask<String, Integer, Book> {
    private String requestUrl;

    protected Book doInBackground(String... url) {
        requestUrl = url[0];
        try {
            return getBooksByAuthor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Book();
    }

    private Book getBooksByAuthor() throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream is = urlConnection.getInputStream();
            JsonObject jsonObject = BooksRetrieveTask.parseResponse(is);
            BookResultsManager bookResultsManager = new BookResultsManager();
            return bookResultsManager.createFirstBook(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return new Book();
    }
}
