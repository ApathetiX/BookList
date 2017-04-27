package com.example.android.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sameetahmed on 4/26/17.
 */

public class BookAdapter extends ArrayAdapter<BookList> {

    public BookAdapter(Context context, List<BookList> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        // Find the earthquake at the given position in the list of earthquakes
        BookList currentBook = getItem(position);

        // Get the original Title string from the Book object
        String title = currentBook.getTitle();

        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        // Display the location of the current title in that TextView
        titleView.setText(title);

        //Get the original Author string from the book object
        String author = currentBook.getAuthor();

        // Find the TextView with view ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);

        // Display the location of the current author in that TextView
        authorView.setText(author);

        // Return the list item view that is now showing the appropriate data
        return listItemView;

    }



}
