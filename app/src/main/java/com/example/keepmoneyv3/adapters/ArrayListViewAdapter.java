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
import java.util.Objects;

public class ArrayListViewAdapter extends ArrayAdapter<DefaultListViewItems> {

    private final ArrayList<DefaultListViewItems> objects;
    private final Context context;

    public ArrayListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DefaultListViewItems> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

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

    @Nullable
    @Override
    public DefaultListViewItems getItem(int position) {
        return objects.get(position);
    }
}
