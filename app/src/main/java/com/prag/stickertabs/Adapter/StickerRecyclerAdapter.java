package com.prag.stickertabs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.prag.stickertabs.Add_item_model;
import com.prag.stickertabs.R;

public class StickerRecyclerAdapter extends FirestoreRecyclerAdapter<Add_item_model, StickerRecyclerAdapter.MyViewHolder> {

    private Context mContext;

    public StickerRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Add_item_model> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.griditem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final Add_item_model add_item_model) {
        String url = add_item_model.getImagepath();
        Glide.with(mContext).load(url).into(myViewHolder.product_img);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView product_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.stker_img);
        }
    }

}

