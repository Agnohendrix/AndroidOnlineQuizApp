package com.example.agnohendrix.androidonlinequizapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Interface.ItemClickListener;
import com.example.agnohendrix.androidonlinequizapp.Interface.RankingCallback;
import com.example.agnohendrix.androidonlinequizapp.Model.QuestionScore;
import com.example.agnohendrix.androidonlinequizapp.Model.Ranking;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.RankingViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {
    View myFragment;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingtbl;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingtbl = database.getReference("Ranking");
        rankingtbl.keepSynced(true);
        questionScore.keepSynced(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        //Firebase orderByChild sorts in ascending order, so i will show it reversed
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateRanking(Common.currentUser.getUserName(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingtbl.child(ranking.getUserName()).setValue(ranking);
                //showRanking(); //Tried for debug
            }
        });

        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                    .setQuery(rankingtbl.orderByChild("score"),Ranking.class).build();

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options
           /*     Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingtbl.orderByChild("score")*/

        ) {
            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ranking, viewGroup, false);
                RankingViewHolder viewHolder = new RankingViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull Ranking model) {
                holder.ranking_name.setText(model.getUserName());
                holder.ranking_score.setText(String.valueOf(model.getScore()));
                if(model.getUserName().equals(Common.currentUser.getUserName())){
                    holder.itemView.setBackgroundColor(Color.GREEN);
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getContext(), getItem(position).getUserName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /*@Override
            protected void populateViewHolder(final RankingViewHolder viewHolder, Ranking model, int position) {
                viewHolder.ranking_name.setText(model.getUserName());
                viewHolder.ranking_score.setText(String.valueOf(model.getScore()));
                if(model.getUserName().equals(Common.currentUser.getUserName())){
                    viewHolder.itemView.setBackgroundColor(Color.GREEN);
                }

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getContext(), getItem(position).getUserName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }*/
        };
        if(Common.currentUser.getUserName() == "Guest" && Common.currentUser.getPassword() == "Guest" && Common.currentUser.getEmail() == "Guest") {
            Toast.makeText(getContext(), "Can't save rankings as Guest", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        adapter.startListening();
        return myFragment;



    }

    private void showRanking() {

        rankingtbl.orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            Ranking local = data.getValue(Ranking.class);
                            Log.d("DEBUG", local.getUserName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateRanking(final String userName, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());
                        }
                        if(Common.currentUser.getUserName() != "Guest" && Common.currentUser.getPassword() != "Guest" && Common.currentUser.getEmail() != "Guest") {
                            Ranking ranking = new Ranking(userName, sum);
                            callback.callBack(ranking);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
