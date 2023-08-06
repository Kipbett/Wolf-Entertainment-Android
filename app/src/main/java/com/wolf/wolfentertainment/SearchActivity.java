package com.wolf.wolfentertainment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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

    private MaterialButton buttonSearch, btn_net;
    private EditText search;
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<MovieModel> movie_list = new ArrayList<>();
    private MovieModel movieModel;
    private AdView adSearch;
    private TextView net_txt;
    private ImageView net_img;
    String search_str;
    String url_all = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonSearch = findViewById(R.id.search_btn);
        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recycerView);
        adSearch = findViewById(R.id.adViewHome);
        btn_net = findViewById(R.id.reload_btn);
        net_txt = findViewById(R.id.net_txt);
        net_img = findViewById(R.id.net_img);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isDataEnabled = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isDataEnabled) {
            queue = Volley.newRequestQueue(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            loadData(url_all);
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_str = search.getText().toString();
                    movie_list.clear();
                    loadData(search_str);
                }
            });
        } else {
            btn_net.setVisibility(View.VISIBLE);
            net_txt.setVisibility(View.VISIBLE);
            net_img.setVisibility(View.VISIBLE);
            btn_net.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adSearch.loadAd(adRequest);
    }

    private void loadData(String search_mv) {
        String url = "http://www.omdbapi.com/?s="+search_mv+"&apikey=42adcc97";

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
                                        searches.getString("Type"),
                                        searches.getString("imdbID"));
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