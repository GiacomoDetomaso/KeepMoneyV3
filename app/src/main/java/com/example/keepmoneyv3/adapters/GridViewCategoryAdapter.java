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
import com.example.keepmoneyv3.utility.Category;


import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is used to manage the building of the GridView, used to display item's and income's
 * categories
 *
 * @author Giacomo Detomaso
 * */
public class GridViewCategoryAdapter extends BaseAdapter {

    private final Context context;//the context of the application
    private final ArrayList<Category> categories;

    public GridViewCategoryAdapter(Context context){
        this.context = context;
        categories = new ArrayList<>();
    }

    /**
     * This override method is used to retrieve the number of items in the adapter
     * */
    @Override
    public int getCount() {
        return categories.size();
    }

    /**
     * This override method is used to retrieve the category at the specified position
     *
     * @param position      the position of the category
     * */
    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This override method is used to build the ListView row by row.
     *
     * @param index         the position of the item in the ListView
     * @param view   the view
     * @param viewGroup        the view group
     *
     * @return listView     a view which contains the ListView
     * */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        View gridView;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = Objects.requireNonNull(inflater).inflate(R.layout.fragment_dashboard_grid_view,null);

            ImageView imageView = gridView.findViewById(R.id.firstImageview);
            TextView textView = gridView.findViewById(R.id.txtImagePc);

            imageView.setImageResource(categories.get(index).getPictureId());
            textView.setText(Objects.requireNonNull(categories.get(index).getName()));
        }
        else {
            gridView = view;
        }

        return gridView;
    }

    /**
     * This method is used to build the items' ArrayList
     *
     * @param catId              the catId
     * @param description       the description of the category
     * @param id                the id of the picture
     * */
    public void buildMap(String catId, String description, int id){
        Category item = new Category(catId,description,id);
        categories.add(item);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
