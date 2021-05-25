package com.example.keepmoneyv3.ui.wishlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;

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
        return root;
    }
}