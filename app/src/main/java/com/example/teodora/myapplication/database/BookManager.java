package com.example.teodora.myapplication.database;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.teodora.myapplication.utils.Book;

public class BookManager extends SQLiteOpenHelper {

    public static final String DATABASE_title = "BookWorld.db";
    public static final String BOOK_TABLE_title = "Book";
    public static final String BOOK_COLUMN_ID = "id";
    public static final String BOOK_COLUMN_TITLE = "title";
    public static final String BOOK_COLUMN_AUTHOR = "author";
    public static final String BOOK_COLUMN_SUBJECT = "subject";
    public static final String BOOK_COLUMN_DATE_READ = "date_read";
    public static final String BOOK_COLUMN_STATUS = "status";

    public static final int BOOK_STATUS_READ = 1;
    public static final int BOOK_STATUS_WISHLIST = 0;

    public BookManager(Context context) {
        super(context, DATABASE_title , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table BOOK " +
                        "(id integer primary key, title text, author text," +
                        " subject text, date_read text, status integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BOOK");
        onCreate(db);
    }


    public ArrayList<Book> getAllBooks() {
        return getBooksByProperty("select * from BOOK ");
    }

    public ArrayList<Book> getAllReadBooks() {
        return getBooksByProperty("select * from BOOK where status = " + BOOK_STATUS_READ);
    }

    public ArrayList<Book> getAllWishlistBooks() {
        return getBooksByProperty("select * from BOOK where status = " + BOOK_STATUS_WISHLIST);
    }

    public Book getBookByTitle() {
        List<Book> res = getBooksByProperty("select * from BOOK where status = " + BOOK_STATUS_WISHLIST);
        if (res != null && !res.isEmpty()) {
            return res.get(0);
        }
        return new Book();
    }

    private ArrayList<Book> getBooksByProperty(String query) {
        ArrayList<Book> books = new ArrayList<Book>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor res = db.rawQuery(query, null)) {
            res.moveToFirst();

            while (!res.isAfterLast()) {
                Book book = new Book();
                book.setTitle(res.getString(res.getColumnIndex(BOOK_COLUMN_TITLE)));
                book.setAuthor(res.getString(res.getColumnIndex(BOOK_COLUMN_AUTHOR)));
                book.setDateRead(res.getString(res.getColumnIndex(BOOK_COLUMN_DATE_READ)));
                List<String> subject = new ArrayList<>();
                subject.add(res.getString(res.getColumnIndex(BOOK_COLUMN_SUBJECT)));
                book.setSubject(subject);
                book.setStatus(res.getInt(res.getColumnIndex(BOOK_COLUMN_STATUS)));
                books.add(book);
                res.moveToNext();
            }
        }
        return books;
    }

    public long insertBook(Book book) {
        String subject = book.getSubject() == null || book.getSubject().isEmpty()
                ? "" : book.getSubject().get(0);
        return insertBook(book.getTitle(), book.getAuthor(), subject, book.getDateRead(), book.getStatus());
    }

    public long insertBook(String title, String author, String subject,
                              String dateRead, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_AUTHOR, author);
        contentValues.put(BOOK_COLUMN_SUBJECT, subject);
        contentValues.put(BOOK_COLUMN_DATE_READ, dateRead);
        contentValues.put(BOOK_COLUMN_STATUS, status);
        return db.insert("BOOK", null, contentValues);

    }

    private boolean insertBookInner(String title, String author, String subject,
                                    String dateRead, int status, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_COLUMN_TITLE, title);
        contentValues.put(BOOK_COLUMN_AUTHOR, author);
        contentValues.put(BOOK_COLUMN_SUBJECT, subject);
        contentValues.put(BOOK_COLUMN_DATE_READ, dateRead);
        contentValues.put(BOOK_COLUMN_STATUS, status);
        db.insert("BOOK", null, contentValues);
        return true;
    }

    public boolean insertBooks(Map<String, Book> books) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        for (Book book : books.values()) {
            String subject = book.getSubject() == null || book.getSubject().isEmpty()
                    ? "" : book.getSubject().get(0);
            insertBookInner(book.getTitle(), book.getAuthor(), subject,
                    book.getDateRead(), book.getStatus(), db);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from BOOK where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BOOK_TABLE_title);
        return numRows;
    }

    public boolean updateBook(Integer id, String title, String author, String subject,
                              String dateRead, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        return updateBookInner(id, title, author, subject, dateRead, status, db);

    }

    private boolean updateBookInner(Integer id, String title, String author, String subject,
                                    String dateRead, int status, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("subject", subject);
        contentValues.put("dateRead", dateRead);
        contentValues.put("status", status);
        db.update("BOOK", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteBook(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("BOOK",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public void truncate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BOOK_TABLE_title, null, null);
    }

}