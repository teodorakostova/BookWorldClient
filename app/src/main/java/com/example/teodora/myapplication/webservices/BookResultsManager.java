package com.example.teodora.myapplication.webservices;

import com.example.teodora.myapplication.utils.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class BookResultsManager {

    public static List<Book> createBooks(JsonObject booksResult) {
        List<Book> books = new ArrayList<>();
        JsonArray jsonArray = booksResult.get("docs").getAsJsonArray();
        int i = 0;
        for (JsonElement jsonElement : jsonArray) {
            if (i > 20) {
                break;
            }
            books.add(createBook(jsonElement));
            i++;
        }
        return books;
    }

    private static Book createBook(JsonElement jsonElement) {
        Book currentBook = new Book();
        currentBook.setTitle(jsonMemberToString(jsonElement, "title"));
        currentBook.setAuthor(jsonMemberToString(jsonElement, "author_name"));
        currentBook.setFirstPublishYear(jsonMemberToInt(jsonElement, "first_publish_year"));
        return currentBook;
    }

    public static Book createFirstBook(JsonObject booksResult) {
        JsonArray jsonArray = booksResult.get("docs").getAsJsonArray();
        return createBook(jsonArray.get(0));
    }

    private static String jsonMemberToString(JsonElement jsonElement, String memberName) {
        JsonElement jsonMemberElement = jsonElement.getAsJsonObject().get(memberName);
        return jsonMemberElement != null ? jsonMemberElement.getAsString() : "";
    }

    private static int jsonMemberToInt(JsonElement jsonElement, String memberName) {
        JsonElement jsonMemberElement = jsonElement.getAsJsonObject().get(memberName);
        return jsonMemberElement != null ? jsonMemberElement.getAsInt() : 0;
    }


}
