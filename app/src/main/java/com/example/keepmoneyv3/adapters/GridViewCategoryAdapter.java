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

public class GridViewCategoryAdapter extends BaseAdapter {

    private final Context context;//the context of the application
    private final ArrayList<Category> categories;

    public GridViewCategoryAdapter(Context context){
        this.context = context;
        categories = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    public void buildMap(String text, String description, int id){
        Category item = new Category(text,description,id);
        categories.add(item);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
