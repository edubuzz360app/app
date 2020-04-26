package com.sss.foody.box;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.shopViewHolder> {

    private Context mContext;
    private List<cardData> myshopList;
    private OnNoteListener mOnNoteListener;
   // ProgressBar progressbar;

    public MyAdapter(Context mContext, List<cardData> myshopList, OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.myshopList = myshopList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public shopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent,
                false);

        return new shopViewHolder(mView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull shopViewHolder holder, int position) {
//        progressbar.setVisibility(View.VISIBLE);

        Picasso.with(mContext)
                .load(myshopList.get(position).getItemImage())
                .fit()
                .noFade()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.morattufoodieplan)
                .into(holder.imageView);

  //      progressbar.setVisibility(View.GONE);

    }


    public interface OnNoteListener {
        void onNoteClick(int position);

    }

    @Override
    public int getItemCount() {
        return myshopList.size();
    }
    public void removeItem(int position) {
        myshopList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(cardData item, int position) {
        myshopList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class shopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView imageView;
        TextView mTitle, mDescription;

        CardView mCardView;
        MyAdapter.OnNoteListener onNoteListener;


        public shopViewHolder(@NonNull View itemView, MyAdapter.OnNoteListener onNoteListener) {
            super(itemView);

           // imageView = itemView.findViewById(R.id.ivImage);
         //   mTitle = itemView.findViewById(R.id.tvTitle);
         //   mDescription = itemView.findViewById(R.id.tvDescription);
            mCardView = itemView.findViewById(R.id.myCardView);

            itemView.setOnClickListener(this);

            this.onNoteListener = onNoteListener;

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
}
