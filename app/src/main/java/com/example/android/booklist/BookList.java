package com.example.android.booklist;

/**
 * Created by sameetahmed on 4/26/17.
 */

public class BookList {

    private String mTitle;

    private String mAuthor;


    public BookList(String title, String author) {
        mTitle = title;

        mAuthor = author;

    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthor() {
        return mAuthor;
    }


}
