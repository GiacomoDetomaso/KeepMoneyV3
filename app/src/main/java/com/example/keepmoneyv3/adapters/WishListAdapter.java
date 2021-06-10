package com.example.keepmoneyv3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepmoneyv3.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to manage the building of the RecyclerView, used to display unconfirmed
 * WishLists
 *
 * @author Michelangelo De Pascale
 * */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WLViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);//a listener used for every items in the recycler clicked
    }

    private final Context context;
    private final ArrayList<ListViewData> items;
    private View root;
    private OnItemClickListener listener;

    public WishListAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }

    /**
     * Create the recycler view
     * */
    @NonNull
    @Override
    public WLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.fragment_wish_lists_items,parent,false);
        return new WLViewHolder(root);
    }

    /**
     * This override method is used to build the various elements of the recycler
     * */
    @Override
    public void onBindViewHolder(@NonNull WLViewHolder holder, final int position) {
        if(items.size() > 0) {
            String totText = "" + items.get(position).getPrice() + " €";
            holder.txtListName.setText(items.get(position).getListName());
            holder.txtTotList.setText(totText);

            root.setOnClickListener(view -> {
                listener.onItemClick(root, position); // specify what happens when a list is clicked
            });
        } else {
            Toast.makeText(context, "Non sono ancora presenti liste", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * The view holder
     * */
     protected static class WLViewHolder extends RecyclerView.ViewHolder{
        TextView txtListName,txtTotList;
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
        ListViewData listViewData = new ListViewData(listName, price);
        items.add(listViewData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * This class represents the purchases' information that will be displayed in the ListView
     * */
    private static class ListViewData implements Serializable {
        private final String listName;
        private final float price;

        public ListViewData(String listName, float price) {
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