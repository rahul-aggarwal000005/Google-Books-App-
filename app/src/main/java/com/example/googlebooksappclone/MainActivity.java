package com.example.googlebooksappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BookClickListener {

    /* Recycler View Adapter to adapt the view holder */
    BooksAdapter booksAdapter = new BooksAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  Text to be searched in the google book */
        EditText searchValue = findViewById(R.id.searchValue);

        /* Search button --> user click this button in order to call the google book api */
        Button searchButton = findViewById(R.id.searchButton);

        /* Setting the recycler view */
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(booksAdapter);

        /* Handling the click listening even on the search button */
        searchButton.setOnClickListener(v -> {
            String search = searchValue.getText().toString();

            /* Formatting the input search to get the most appropriate result */
            String[] searchTextArray = search.split(" ");
            StringBuilder searchText = new StringBuilder();
            for (int i = 0; i < searchTextArray.length; i++) {
                if (searchTextArray[i].length() > 0) {
                    searchText.append(searchTextArray[i]).append(i == searchTextArray.length - 1 ? "" : "_");
                }
            }
            /* These are the cases if the user does not send the right input */
            if (search.length() == 0) {
                Toast.makeText(MainActivity.this, "Please enter the name of the book", Toast.LENGTH_SHORT).show();
            } else {
                getData(searchText.toString());
            }
        });
    }

    public void getData(String search) {
        ArrayList<BookItem> Books = new ArrayList<>();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + search + "&orderBy=newest&key=AIzaSyAZyz_9aOkZX7Zli492-Exhd-WsD4miNvk";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, response -> {
            try {
                JSONArray BooksResult = response.getJSONArray("items");
                int size = Math.min(BooksResult.length(), 10);
                for (int i = 0; i < size; i++) {
                    JSONObject BookItem = BooksResult.getJSONObject(i);
                    JSONObject volumeInfo = BookItem.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String description = volumeInfo.has("description") ? volumeInfo.getString("description") : "No Description is Provided.";
                    String previewLink = volumeInfo.has("previewLink") ? volumeInfo.getString("previewLink") : "https://www.google.com/";
                    StringBuilder authorsText = new StringBuilder("Not Available");
                    if (volumeInfo.has("authors")) {
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        authorsText.setLength(0);
                        for (int j = 0; j < authors.length(); j++) {
                            authorsText.append(authors.getString(j)).append(j == authors.length() - 1 ? "." : ",");
                        }
                    }

                    String imageLink = "https://veiindia.com/wp-content/uploads/2018/11/book-image-not-available.png";

                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinkObject = volumeInfo.getJSONObject("imageLinks");
                        imageLink = imageLinkObject.getString("thumbnail");
                    }

                    Books.add(new BookItem(title, authorsText.toString(), description, imageLink, previewLink));
                }
                booksAdapter.updateBooks(Books);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // TODO: Handle error
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBookItemClickListener(BookItem book) {
        String url = book.getPreviewLink();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}