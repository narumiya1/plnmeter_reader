package com.example.myapplicationpln.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MHistory;

import java.util.ArrayList;

public class HistoryFragment3 extends RecyclerView.Adapter<HistoryFragment3.ViewHolder> {
    private ArrayList<MHistory> dataModalArrayList;
    private Context context;

    // constructor class for our Adapter
    public HistoryFragment3(ArrayList<MHistory> dataModalArrayList, Context context) {
        this.dataModalArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryFragment3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new HistoryFragment3.ViewHolder(LayoutInflater.from(context).inflate(R.layout.content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryFragment3.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        final MHistory modal = dataModalArrayList.get(position);
        String meter = String.valueOf(modal.getMeter());
        holder.courseNameTV.setText(meter);

        // we are using Picasso to load images
        // from URL inside our image view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting on click listener
                // for our items of recycler items.
                Toast.makeText(context, "Clicked item is " + modal.getMeter(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our
        // views of recycler items.
        private TextView courseNameTV;
        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing the views of recycler views.
            courseNameTV = itemView.findViewById(R.id.idTVtext);
        }
    }
}
