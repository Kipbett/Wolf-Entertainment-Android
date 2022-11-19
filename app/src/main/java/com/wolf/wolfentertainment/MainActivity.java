package com.wolf.wolfentertainment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Visibility;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView ivposter;
    private TextView tvTittle, tvGenre, tvMvGenre, tvActors, tvMvActors, tvRatings, tvMvRatings, tvPlot, tvRuntime, tvMvRuntime;
    private String searchTitle, searchYear, year = "";
    private AdView adView;
    private MaterialButton btn_trailer;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extra = getIntent().getExtras();
        ivposter =findViewById(R.id.poster);
        tvTittle =findViewById(R.id.tvTittle);
        tvGenre =findViewById(R.id.tvGenre);
        tvMvGenre =findViewById(R.id.tvMvGenre);
        tvActors =findViewById(R.id.tvActors);
        tvMvActors =findViewById(R.id.tvMvActors);
        tvRatings =findViewById(R.id.tvRatings);
        tvMvRatings =findViewById(R.id.tvMvRatings);
        tvPlot =findViewById(R.id.tvPlot);
        tvRuntime = findViewById(R.id.tvRuntime);
        tvMvRuntime = findViewById(R.id.tvMvRuntime);
        adView = findViewById(R.id.adView);
        btn_trailer = findViewById(R.id.trailer_button);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        btn_trailer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Coming Soon\n Be Patient", Toast.LENGTH_SHORT).show();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        searchTitle = extra.getString("title");
        searchYear = extra.getString("year");
        getSupportActionBar().setTitle(searchTitle);
        if(searchYear.length() > 4)
            year = searchYear.substring(0,4);
        else
            year = searchYear;

        requestQueue = Volley.newRequestQueue(this);

        getData();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void getData() {

        String url = "http://www.omdbapi.com/?t="+searchTitle+"&y="+year+"&apikey=42adcc97";

        JsonObjectRequest movie = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title =response.getString("Title");
                            String genre = response.getString("Genre");
                            String actors = response.getString("Actors");
                            String poster = response.getString("Poster");
                            String ratings = response.getString("imdbRating");

                            Glide.with(MainActivity.this).load(poster).into(ivposter);
                            tvTittle.setText(title);
                            tvMvGenre.setText(genre);
                            tvMvActors.setText(actors);
                            tvMvRatings.setText(ratings);
                            tvPlot.setText(response.getString("Plot"));
                            tvMvRuntime.setText(response.getString("Runtime"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(movie);
    }
}