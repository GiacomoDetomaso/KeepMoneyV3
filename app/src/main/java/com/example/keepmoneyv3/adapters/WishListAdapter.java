package com.example.keepmoneyv3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepmoneyv3.R;

import java.io.Serializable;
import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WLViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);//a listener used for every items in the recycler clicked
    }

    private final Context context;
    private final ArrayList<ListPurchases> items;
    private View root;
    private OnItemClickListener listener;

    public WishListAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public WLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.fragment_wish_lists_items,parent,false);
        return new WLViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull WLViewHolder holder, final int position) {
        if(items.size() > 0) {
            String totText = "" + items.get(position).getPrice() + " €";
            holder.txtListName.setText(items.get(position).getListName());
            holder.txtTotList.setText(totText);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(root, position); // specify what happens when a list is clicked
                }
            });
        } else {
            Toast.makeText(context, "Non sono ancora presenti liste", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

     protected static class WLViewHolder extends RecyclerView.ViewHolder{
        TextView txtListName,txtTotList;
        ImageView icon;
        CardView cardView;
        WLViewHolder(View itemView){
            super(itemView);
            txtListName = itemView.findViewById(R.id.txtListName);
            txtTotList = itemView.findViewById(R.id.txtTotalWl);
            cardView = itemView.findViewById(R.id.cardViewItem);
        }
    }

    /**
     * This method is used to add new ListPurchases objects in the ArrayList
     *
     * @param listName      the name of the item
     * @param price         the price of the purchase
     * */
    public void buildMap(String listName,float price){
        WishListAdapter.ListPurchases listPurchases = new WishListAdapter.ListPurchases(listName, price);
        items.add(listPurchases);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * This class represents the purchases' information that will be displayed in the ListView
     * */
    private static class ListPurchases implements Serializable {
        private final String listName;
        private final float price;

        public ListPurchases(String listName,float price) {
            this.listName = listName;
            this.price = price;
        }

        public String getListName() {
            return listName;
        }


        public float getPrice() {
            return price;
        }
    }

}