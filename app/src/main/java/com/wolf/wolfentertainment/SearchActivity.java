package com.wolf.wolfentertainment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.wolf.wolfentertainment.adapter.MovieAdapter;
import com.wolf.wolfentertainment.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private MaterialButton buttonSearch;
    private EditText search;
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<MovieModel> movie_list = new ArrayList<>();
    private MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonSearch = findViewById(R.id.buttonSearch);
        search = findViewById(R.id.eSearch);
        recyclerView = findViewById(R.id.recycerView);

        queue = Volley.newRequestQueue(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie_list.clear();
                loadData();
            }
        });
    }

    private void loadData() {
        String search_str = search.getText().toString();
        String url = "http://www.omdbapi.com/?s="+search_str+"&apikey=42adcc97";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Search");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject searches = jsonArray.getJSONObject(i);
                                movieModel = new MovieModel(searches.getString("Poster"),
                                        searches.getString("Title"),
                                        searches.getString("Year"),
                                        searches.getString("Type"));
                                movie_list.add(movieModel);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SearchActivity.this, "ERROR: "+e, Toast.LENGTH_SHORT).show();
                        }

                        MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movie_list);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(objectRequest);

    }
}