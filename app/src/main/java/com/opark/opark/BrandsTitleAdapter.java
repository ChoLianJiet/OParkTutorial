package com.opark.opark;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BrandsTitleAdapter extends RecyclerView.Adapter<BrandsTitleAdapter.BrandsTitleAdapterViewHolder> {
    Context mContext;
    private BrandsTitleAdapter.BrandsSelected brandsSelected;

    public interface BrandsSelected{
        void onBrandsSelected(View v, int position);

    }




    public class BrandsTitleAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView brandsTitle;



        public BrandsTitleAdapterViewHolder(View itemView) {
            super(itemView);


            brandsTitle = itemView.findViewById(R.id.brands_full_title);

        }
    }

    List<BrandsName> brandsNameList;
    public BrandsTitleAdapter(List<BrandsName> brandsNameList, BrandsTitleAdapter.BrandsSelected brandsSelected){
        this.brandsNameList = brandsNameList;
        this.brandsSelected = brandsSelected;
//        this.originalList = new ArrayList<>(merchantOfferList);

    }





    @NonNull
    @Override
    public BrandsTitleAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_title_view2, parent, false);
        BrandsTitleAdapterViewHolder brandsTitleAdapterViewHolder = new BrandsTitleAdapterViewHolder(v);
        return brandsTitleAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BrandsTitleAdapterViewHolder holder, final int position) {

        holder.brandsTitle.setText(brandsNameList.get(position).getBrandsName());


        holder.brandsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("selected", "ori class brandsselect interface :   " +  holder.brandsTitle.getText().toString());
                brandsSelected.onBrandsSelected(view,position);
                BrandsOfferFragment1.unityName= holder.brandsTitle.getText().toString();






            }
        });



    }

    @Override
    public int getItemCount() {
        return brandsNameList.size();
    }
}
