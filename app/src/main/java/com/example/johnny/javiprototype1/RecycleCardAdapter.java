package com.example.johnny.javiprototype1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecycleCardAdapter extends RecyclerView.Adapter<RecycleCardAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView card;
        public ImageView picture;
        public TextView firstText;
        public TextView secondText;
        public ViewHolder(View v) {
            super(v);
            card =(CardView) v.findViewById(R.id.cv);
            picture = (ImageView) v.findViewById(R.id.photo);
            firstText = (TextView) v.findViewById(R.id.text1);
            secondText = (TextView) v.findViewById(R.id.text2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleCardAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.picture.setImageResource(R.drawable.koala);
        holder.firstText.setText(mDataset[position]);
        holder.secondText.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
