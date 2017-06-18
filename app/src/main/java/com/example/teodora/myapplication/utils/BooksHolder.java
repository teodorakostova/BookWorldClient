package com.example.teodora.myapplication.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Teodora on 6/17/2017.
 */

public final class BooksHolder {
    private static Map<String, Book> alreadyReadBooks = new HashMap<>();

    private static Map<String, Book> wishlistBooks = new HashMap<>();

    public static Map<String, Book> getWishlistBooks() {
        return wishlistBooks;
    }

    public static void setWishlistBook(Map<String, Book> wishlistBook) {
        BooksHolder.wishlistBooks = wishlistBook;
    }

    public static Map<String, Book> getAlreadyReadBooks() {
        return alreadyReadBooks;
    }

    public static void setAlreadyReadBooks(Map<String, Book> alreadyReadBooks) {
        BooksHolder.alreadyReadBooks = alreadyReadBooks;
    }

    public static void addAlreadyReadBook(Book book) {
        BooksHolder.alreadyReadBooks.put(book.getTitle(), book);
    }

    public static void addWishlistBook(Book book) {
        BooksHolder.wishlistBooks.put(book.getTitle(), book);
    }

    public static void removeAlreadyReadBook(Book book) {
        BooksHolder.alreadyReadBooks.remove(book.getTitle());
    }

    public static void removeWishlistBook(String book) {
        BooksHolder.wishlistBooks.remove(book);
    }

    public Book getWishlistBookByTitle (String title){
        return BooksHolder.wishlistBooks.get(title);
    }

    public Book getAlreadyReadBookByTitle (String title){
        return BooksHolder.alreadyReadBooks.get(title);
    }
}
