package com.example.android.booklist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklist.MainActivity.LOG_TAG;

/**
 * Created by sameetahmed on 4/26/17.
 */

public final class Utilities {

    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE = "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Create a private constructor because no one should ever create a {@link Utilities} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of Utilities is not needed).
     */
    private Utilities() {
    }

    /**
     * Query the USGS dataset and return a list of {@link BookList} objects.
     */
    public static List<BookList> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<BookList> books = extractBooks(jsonResponse);

        // Return the list of {@link Earthquake}s
        return books;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link BookList} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<BookList> extractBooks(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<BookList> books = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of books.
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link BookLists} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of earthquakes
                JSONObject currentBook = bookArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all volumes
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                // Extract the value for the key called "author"
                String author = volumeInfo.getString("authors");

                // Create a new {@link BookList} object with title, and author from the JSON response.
                BookList book = new BookList(title, author);

                // Add the new {@link BookList} to the list of earthquakes.
                books.add(book);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("Utilities", "Problem parsing the book JSON results", e);
        }
        //Returns the list of books
        return books;
    }


}
