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

/**
 * This class is used to manage the building of the ExpandableListView,
 * used to display user's confirmed WishLists and their items
 *
 * @author Giacomo Detomaso
 * */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final HashMap<String, ArrayList<DefaultListViewItems>> map;
    private final ArrayList<String> titles;

    public ExpandableListViewAdapter(Context context, HashMap <String, ArrayList<DefaultListViewItems>> map, ArrayList<String> titles){
        this.context = context;
        this.map = map;
        this.titles = titles;
    }

    /**
     * This override method is used to get the number of the groups
     * */
    @Override
    public int getGroupCount() {
        return titles.size();
    }

    /**
     * This override method is used get the number of children of a group
     *
     * @param groupPosition     the position of a group
     * */
    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(map.get(titles.get(groupPosition))).size();
    }

    /**
     * This override method is used to get a specified group
     *
     * @param groupPosition     the position of a group
     * */
    @Override
    public String getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    /**
     * This override method is used to get a child from the specified group
     *
     * @param groupPosition     the position of a group
     * @param childPosition     the position of the child
     * */
    @Override
    public DefaultListViewItems getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.map.get(this.titles.get(groupPosition)))
                .get(childPosition);
    }

    /**
     * This override method is used to get the id of the group
     * */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * This override method is used to get the id of a child of a group
     * */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * This override method is used to build the group view of the ExpandableListView row by row
     *
     * @param groupPosition     the position of a group
     * @param view              the view
     * @param viewGroup         the viewGroup
     * */
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

    /**
     * This override method is used to build the child view of the ExpandableListView row by row
     *
     * @param groupPosition     the position of a group
     * @param childPosition     the position of the child
     * @param view              the view
     * @param viewGroup         the viewGroup
     * */
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
