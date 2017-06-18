package com.example.teodora.myapplication.webservices;

import android.os.AsyncTask;

import com.example.teodora.myapplication.utils.Book;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BooksRetrieveTask extends AsyncTask<String, Integer, List<Book>> {
    private String requestUrl;

    @Override
    protected List<Book> doInBackground(String... url) {
        requestUrl = url[0];
        try {
            return getBooksByAuthor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Book> getBooksByAuthor() throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUrl);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream is = urlConnection.getInputStream();
            JsonObject jsonObject = parseResponse(is);
            BookResultsManager bookResultsManager = new BookResultsManager();
            return bookResultsManager.createBooks(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return new ArrayList<Book>();
    }

    public static JsonObject parseResponse(InputStream inputStream) throws UnsupportedEncodingException {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(
                new InputStreamReader(inputStream, "UTF-8"));
        return jsonElement.getAsJsonObject();
    }
}
