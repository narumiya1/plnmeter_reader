package com.example.myapplicationpln.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private View mView;
    RecyclerView mRecyclerview;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private LinearLayoutManager mManager;
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private Query needsQuery;
    private Query offersQuery;
    SessionPrefference session;
    private Dialog dialog;
    LineChart lineChart ;
    DatabaseReference databaseReference2;


    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyi, container, false);
        // Inflate the layout for this fragment
        DatabaseReference referenceHistor = FirebaseDatabase.getInstance().getReference();

        dialog = new Dialog(getActivity());

        session = new SessionPrefference(getActivity());
        mRecyclerview = view.findViewById(R.id.recyclerViewHistory);
        mRecyclerview.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);

        Log.e(TAG, "firebaseoption");

        //add string & get from session
        String userId = session.getUserId();
        Log.d("get userzId Historiy", " : " + userId);

        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mDatabase.getReference().child("History").orderByChild("id_user").equalTo(userId);
        mUserDatabase2 = mDatabase.getReference().child("History");
        Query queryhistory = referenceHistor.child("History").child("33");
        queryhistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount()==0){
                        Log.d("DATA < 0 Historyi", "onDataChange: " + dataSnapshot.getValue());
                    }else {
                        Log.d("DATA Historyi > 0 ", "onDataChange: \n" + dataSnapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        Rumus Firebase one to many
        mUserDatabase = mDatabase.getReference().child("User").orderByChild("address").equalTo("Kilmarnock");
        firebase.database().ref('Address').orderBy('User').equalTo('id');
        needsQuery = mUserDatabase.orderByChild("address").equalTo("Kilmarnock");
        offersQuery = mUserDatabase.orderByChild("type").equalTo("offer");
         */


        FirebaseRecyclerOptions<MHistory> options =
                new FirebaseRecyclerOptions.Builder<MHistory>().
                        setQuery(mUserDatabase, MHistory.class).
                        build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MHistory, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull MHistory model) {
                Log.d("stringset"," "+model.getMeter());
                holder.setFname(model.getMeter());
                holder.setLname(model.getScore_classfy());
                holder.setAname(model.getScore_identfy());
                holder.setDate(model.getCreated_at());
                int status = 1;
                holder.setStatusName(1);
                Log.d("get getMeter ", " : " + model.getMeter());
                dialog = new Dialog(getActivity());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.setTitle("Item");
                        dialog.setContentView(R.layout.dialog_chart_design);
                        TextView oke = dialog.findViewById(R.id.oke);
                        PieChart pieChart;
                        pieChart = dialog.findViewById(R.id.chart1);
                        pieChart.setUsePercentValues(true);
                        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                        float f = Float.parseFloat(String.valueOf(model.getMeter()));
                        yvalues.add(new PieEntry((float) f, model.getMeter()));
                        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.gcm_defaultSenderId));
                        PieData data = new PieData(dataSet);

                        data.setValueFormatter(new PercentFormatter());
                        pieChart.setData(data);
                        Description description = new Description();
                        description.setText(getString(R.string.project_id));
                        pieChart.setDescription(description);
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setTransparentCircleRadius(58f);
                        pieChart.setHoleRadius(58f);
                        pieChart.setEntryLabelColor(Color.BLACK);
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        data.setValueTextSize(13f);
                        data.setValueTextColor(Color.BLACK);
                        Button button = dialog.findViewById(R.id.cancel);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                            }
                        });

                        dialog.show();
                    }
                });

                /*
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Context context = view.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("userAddress", model.getAlamat_pelanggan());
                        intent.putExtra("userDetailId", model.getId_pelanggan());
                        intent.putExtra("userAddressId", model.getUser_address_id());

                        Log.d("Body model model", "model: " + model.getAlamat_pelanggan());
                        context.startActivity(intent);


                    }
                });

                 */
            }


            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_history, parent, false);
                Log.e(TAG, "oncreate is called");

                return new UserViewHolder(view);
            }
        };
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    //ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardView = itemView.findViewById(R.id.cvMain_addresss);

            /*
                mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, EventDetailActivity.class);

                    context.startActivity(intent);
                }
              });

             */
        }

        public void setStatusName(int status){
            TextView status0 = (TextView) mView.findViewById(R.id.status0);
            TextView status1 = (TextView) mView.findViewById(R.id.status1);
            ImageView imageView = mView.findViewById(R.id.img_check_done);
            ImageView imageView2 = mView.findViewById(R.id.img_check_done_all);
            if (status<=0){
                status0.setVisibility(View.GONE);
                status1.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);

            }else{
                status0.setVisibility(View.GONE);
                status1.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                imageView2.setVisibility(View.VISIBLE);
            }
        }
        public void setFname(double name) {
            TextView FName = (TextView) mView.findViewById(R.id.rMeter);
            String s=Double.toString(name);
            FName.setText(s);
        }


        public void setLname(double name) {
            TextView LName = (TextView) mView.findViewById(R.id.rIdentify);
            String s=Double.toString(name);
            Log.d("stringset"," "+s);
            LName.setText(s);
        }

        public void setAname(double name) {
            TextView AName = (TextView) mView.findViewById(R.id.rClassify);
            String s=Double.toString(name);
            Log.d("stringset"," "+s);
            AName.setText(s);
        }

        public void setDate(String d) {
            TextView DName = (TextView) mView.findViewById(R.id.txtDates);
            DName.setText(d);
        }

        public void setOrderSpinner(String status) {

        }

    }


}