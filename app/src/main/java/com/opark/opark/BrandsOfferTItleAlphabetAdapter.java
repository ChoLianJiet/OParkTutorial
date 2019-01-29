package com.opark.opark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BrandsOfferTItleAlphabetAdapter extends RecyclerView.Adapter<BrandsOfferTItleAlphabetAdapter.AlphabetTextViewHolder> {

    Context mContext;
    private List<BrandsAlphabet> brandsOffer;
    private  BrandsSelected1 brandsSelected;

    public interface BrandsSelected1{
        void onButtonClicked(View v, int position);

    }

    DatabaseReference brandsTitleAlphabetDatBase = FirebaseDatabase.getInstance().getReference().child("merchantsName/");


    public static class AlphabetTextViewHolder extends RecyclerView.ViewHolder {
        public TextView brandsAlphabet;


         AlphabetTextViewHolder(View v) {
            super(v);
//            brandsAlphabet = v.findViewById(R.id.brands_title_alphabet);

        }
    }





    List<BrandsAlphabet> brandsAlphabetList;
    public BrandsOfferTItleAlphabetAdapter(List<BrandsAlphabet> brandsAlphabetList, BrandsSelected1 brandsSelected){
        this.brandsAlphabetList = brandsAlphabetList;
        this.brandsSelected = brandsSelected;
//        this.originalList = new ArrayList<>(merchantOfferList);

    }




    @NonNull
    @Override
    public AlphabetTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_title_view, parent,false);
        AlphabetTextViewHolder alphabetTextViewHolder= new AlphabetTextViewHolder(view);

        return alphabetTextViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlphabetTextViewHolder holder, int position) {



        try {
            Log.d("brands", "onBindViewHolder:  " + brandsAlphabetList.get(position).getBrandsAlphabet());

                    Character alphabet = brandsAlphabetList.get(position).getBrandsAlphabet();

            holder.brandsAlphabet.setText(alphabet);

        }catch  (NullPointerException e){
            e.printStackTrace();
        }
//


//        Log.d("brands", "onBindViewHolder:  " +holder.brandsAlphabet.getText()  );

    }


    @Override
    public int getItemCount() {

//
//        Log.d("brands", "getItemCount: this  " +brandsAlphabetList.get(0).getBrandsAlphabet());
//        Log.d("brands", "getItemCount:  " +brandsAlphabetList.size());
        return brandsAlphabetList.size();
    }




}
