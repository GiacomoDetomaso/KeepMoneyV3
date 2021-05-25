package com.example.keepmoneyv3.ui.wishlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.adapters.WishListAdapter;

public class WishListsFragment extends Fragment {

    //private WishListsViewModel wishListsViewModel;
    public interface WishListsFragmentListener{
         void onWishListsFragmentOpened();
    }

    private WishListsFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (WishListsFragment.WishListsFragmentListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listener.onWishListsFragmentOpened(); // notify the NavigationActivity
        View root = inflater.inflate(R.layout.fragment_wish_lists, container, false);

        final int SPAN_COUNT = 2;
        CardView cardViewWishList = root.findViewById(R.id.cardViewWishList);
        RecyclerView recyclerView = cardViewWishList.findViewById(R.id.wishListRecyclerView);
        WishListAdapter wishListAdapter = new WishListAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));

        recyclerView.setAdapter(wishListAdapter);
        return root;
    }
}