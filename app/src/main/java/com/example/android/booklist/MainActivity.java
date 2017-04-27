package com.example.android.booklist;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookList>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for Google Books API
     */
    private static String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * BookAdapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    private EditText mEditText;

    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference to the EditText in the layout
        mEditText = (EditText) findViewById(R.id.edit_query);

        //Find a reference to the ButtonView in the layout
        mButton = (Button) findViewById(R.id.search_button);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<BookList>());

        // Find a reference to the {@link ListView} in the layout
        final ListView bookListView = (ListView) findViewById(R.id.list);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resets the UI on button click
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                //Store the query
                String searchQuery = mEditText.getText().toString().trim();

                // If there was text in the query then add it to the URL
                if (searchQuery != null && searchQuery != "") {
                    BOOK_URL += searchQuery;
                }

                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                bookListView.setAdapter(mAdapter);


            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }

    }


    @Override
    public Loader<List<BookList>> onCreateLoader(int i, Bundle args) {
        // Create a new loader for the given URL
        return new BookLoader(this, BOOK_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<BookList>> loader, List<BookList> books) {
        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link books}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<BookList>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }
}



