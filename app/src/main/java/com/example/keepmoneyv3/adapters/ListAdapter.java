package com.example.keepmoneyv3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.utility.DefaultListViewItems;

import java.util.ArrayList;
import java.util.Objects;

public class ListAdapter extends BaseAdapter {
    private final Context context;//the context of the application
    private final ArrayList<DefaultListViewItems>items;

   public ListAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
   }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }//return the position

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View listView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = Objects.requireNonNull(inflater).inflate(R.layout.default_list_view_item, null);

            ImageView imageView = listView.findViewById(R.id.firstImageview2); // image of the object
            TextView txtObj = listView.findViewById(R.id.textView); // object
            TextView txtPrice = listView.findViewById(R.id.txtPrice); // price of the object

            // set the proper values to the elements
            imageView.setImageResource(items.get(i).getImage());
            txtObj.setText(Objects.requireNonNull(items.get(i).getItemName()));
            String price = "" + items.get(i).getPrice() + " €";
            txtPrice.setText(price);
        }else {
            listView = view;
        }

        return listView;
    }

    /**
     * This method is used to add new ListPurchases objects in the ArrayList
     *
     * @param itemName      the name of the item
     * @param image         the image of the category of the item
     * @param price         the price of the purchase
     * */
   public void buildMap(int id, String itemName, int image, float price){
        DefaultListViewItems defaultListViewItems = new DefaultListViewItems(id, itemName, image, price);
        items.add(defaultListViewItems);
    }

    public void buildMap(ArrayList<DefaultListViewItems> defaultListPurchases, int sort){
        
    }




}
