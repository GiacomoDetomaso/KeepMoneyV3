package com.example.keepmoneyv3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.utility.DefaultListViewItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
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

    public void buildMap(ArrayList<DefaultListViewItems> defaultListViewItems, int sort){
        switch (sort) {
            case 0:
                for (int i = 0; i < defaultListViewItems.size(); i++) {
                    items.add(defaultListViewItems.get(i));
                }
                break;
            case 1:
                quickSort(defaultListViewItems,0, defaultListViewItems.size()-1);
                for (int i = 0; i < defaultListViewItems.size(); i++) {
                    items.add(defaultListViewItems.get(i));
                }
                break;
            case 2:
                Toast.makeText(context.getApplicationContext(), "Funzione implementata presto!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    static int partition(ArrayList<DefaultListViewItems> arr, int low, int high) {
        float pivot = arr.get(high).getPrice();

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++) {
            if (arr.get(j).getPrice() < pivot) {
                i++;
                Collections.swap(arr,i,j);
            }
        }
        Collections.swap(arr,i+1,high);
        return (i + 1);
    }

    static void quickSort(ArrayList<DefaultListViewItems> arr, int low, int high) {
        if (low < high) {
            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = partition(arr, low, high);

            // Separately sort elements before
            // partition and after partition
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }



}
