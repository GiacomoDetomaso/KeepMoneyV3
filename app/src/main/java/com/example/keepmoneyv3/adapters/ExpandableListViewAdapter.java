package com.example.keepmoneyv3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.utility.DefaultListViewItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final HashMap<String, ArrayList<DefaultListViewItems>> map;
    private final ArrayList<String> titles;

    public ExpandableListViewAdapter(Context context, HashMap <String, ArrayList<DefaultListViewItems>> map, ArrayList<String> titles){
        this.context = context;
        this.map = map;
        this.titles = titles;
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(map.get(titles.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.map.get(this.titles.get(groupPosition)))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        View listView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = Objects.requireNonNull(inflater).inflate(R.layout.fragment_movements_wish_lists_expandable_list_view_title, null);

            TextView txtTile = listView.findViewById(R.id.listTitle);

            String title = (String) getGroup(groupPosition);
            txtTile.setText(title);

        }else {
            listView = view;
        }

        return listView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        View listView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = Objects.requireNonNull(inflater).inflate(R.layout.default_list_view_item, null);

            ImageView imageView = listView.findViewById(R.id.firstImageview2); // image of the object
            TextView txtObj = listView.findViewById(R.id.textView); // object
            TextView txtPrice = listView.findViewById(R.id.txtPrice); // price of the object

            DefaultListViewItems defaultListViewItemsArrayList = (DefaultListViewItems) getChild(groupPosition, childPosition);

            // set the proper values to the elements
            imageView.setImageResource(defaultListViewItemsArrayList.getImage());
            txtObj.setText(Objects.requireNonNull(defaultListViewItemsArrayList.getItemName()));
            String price = "" + defaultListViewItemsArrayList.getPrice() + " €";
            txtPrice.setText(price);
        }else {
            listView = view;
        }

        return listView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
