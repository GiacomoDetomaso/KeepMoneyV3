package com.example.keepmoneyv3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.utility.DefaultListViewItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * This class is used to manage the building of the ListView, used to display user's movements
 *
 * @author Giacomo Detomaso
 * */
public class ArrayListViewAdapter extends ArrayAdapter<DefaultListViewItems> {

    private final ArrayList<DefaultListViewItems> objects;
    private final Context context;

    public ArrayListViewAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        objects = new ArrayList<>();
    }

    /**
     * This override method is used to retrieve the number of items in the adapter
     * */
    @Override
    public int getCount() {
        return objects.size();
    }

    /**
     * This override method is used to build the ListView row by row.
     *
     * @param position      the position of the item in the ListView
     * @param convertView   the view
     * @param parent        the view group
     *
     * @return listView     a view which contains the ListView
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = Objects.requireNonNull(inflater).inflate(R.layout.default_list_view_item, null);

            ImageView imageView = listView.findViewById(R.id.firstImageview2); // image of the object
            TextView txtObj = listView.findViewById(R.id.textView); // object
            TextView txtPrice = listView.findViewById(R.id.txtPrice); // price of the object

            // set the proper values to the elements
            imageView.setImageResource(objects.get(position).getImage());
            txtObj.setText(Objects.requireNonNull(objects.get(position).getItemName()));
            String price = "" + objects.get(position).getPrice() + " €";
            txtPrice.setText(price);
        }else {
            listView = convertView;
        }

        return listView;
    }

    /**
     * This override method is used to retrieve the item of the ListView specified by
     * a position
     *
     * @param position      the position of the item
     * @return The item to return
     * */
    @Nullable
    @Override
    public DefaultListViewItems getItem(int position) {
        return objects.get(position);
    }

    /**
     * This method is used to add a single DefaultListViewItems object in the ArrayList
     *
     * @param itemName      the name of the item
     * @param image         the image of the category of the item
     * @param price         the price of the purchase
     * */
    public void buildMap(int id, String itemName, int image, float price){
        DefaultListViewItems defaultListViewItems = new DefaultListViewItems(id, itemName, image, price);
        objects.add(defaultListViewItems);
    }

    /**
     * This method is used to add all the DefaultListViewItems objects in the ArrayList,
     * with the option to sort them
     *
     * @param defaultListViewItems      the ArrayList
     * @param sort                      indicates if the ArrayList has to be sorted
     * */
    public void buildMap(ArrayList<DefaultListViewItems> defaultListViewItems, int sort){
        switch (sort) {
            case 0:
                objects.addAll(defaultListViewItems);
                break;
            case 1:
                quickSort(defaultListViewItems,0, defaultListViewItems.size()-1);
                objects.addAll(defaultListViewItems);
                break;
            case 2:
                reverseQuickSort(defaultListViewItems,0, defaultListViewItems.size()-1);
                objects.addAll(defaultListViewItems);
                break;
        }
    }

    /**
     * Method to create a partition of the given array (ASC)
     *
     * @param arr       the array to sort
     * @param low       the lower bound of the array
     * @param high      the upper bound of the array
     * */
    static int partition(ArrayList<DefaultListViewItems> arr, int low, int high) {
        float pivot = arr.get(high).getPrice();

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++) {
            if (arr.get(j).getPrice() < pivot) {
                i++;
                Collections.swap(arr,i,j); // swap the elements
            }
        }
        Collections.swap(arr,i+1,high);
        return (i + 1);
    }

    /**
     * Recursive method to perform the quicksort (ASC)
     *
     * @param arr       the array to sort
     * @param low       the lower bound of the array
     * @param high      the upper bound of the array
     * */
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

    /**
     * Recursive method to perform the quicksort (DESC)
     *
     * @param arr       the array to sort
     * @param low       the lower bound of the array
     * @param high      the upper bound of the array
     * */
    static void reverseQuickSort(ArrayList<DefaultListViewItems> arr, int low, int high) {
        if (low < high) {
            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = reversePartition(arr, low, high);

            // Separately sort elements before
            // partition and after partition
            reverseQuickSort(arr, low, pi - 1);
            reverseQuickSort(arr, pi + 1, high);
        }
    }

    /**
     * Method to create a partition of the given array (DESC)
     *
     * @param arr       the array to sort
     * @param low       the lower bound of the array
     * @param high      the upper bound of the array
     * */
    static int reversePartition(ArrayList<DefaultListViewItems> arr, int low, int high) {
        float pivot = arr.get(high).getPrice();

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++) {
            if (arr.get(j).getPrice() > pivot) {
                i++;
                Collections.swap(arr,i,j);
            }
        }
        Collections.swap(arr,i+1,high);
        return (i + 1);
    }

}
