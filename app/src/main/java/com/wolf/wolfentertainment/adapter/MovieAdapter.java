package com.wolf.wolfentertainment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wolf.wolfentertainment.MainActivity;
import com.wolf.wolfentertainment.R;
import com.wolf.wolfentertainment.SearchActivity;
import com.wolf.wolfentertainment.model.MovieModel;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterHolder> {

    private Context context;
    private List<MovieModel> movieList;

    public MovieAdapter(Context context, List<MovieModel> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
        return new MovieAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterHolder holder, int position) {
        MovieModel mm = movieList.get(position);

        Glide.with(context).load(mm.getPoster()).into(holder.lyImage);
        holder.lyRuntime.setText(mm.getType());
        holder.lyYear.setText(mm.getYear());
        holder.lyTitle.setText(mm.getTitle());
        holder.lyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", mm.getTitle());
                intent.putExtra("year", mm.getYear());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieAdapterHolder extends RecyclerView.ViewHolder {
        ImageView lyImage;
        TextView lyTitle, lyYear, lyRuntime;
        public MovieAdapterHolder(@NonNull View itemView) {
            super(itemView);

            lyImage = itemView.findViewById(R.id.lyImage);
            lyTitle = itemView.findViewById(R.id.lyTitle);
            lyYear = itemView.findViewById(R.id.lyYear);
            lyRuntime = itemView.findViewById(R.id.lyType);
        }
    }
}
