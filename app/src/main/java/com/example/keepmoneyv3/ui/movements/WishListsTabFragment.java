package com.example.keepmoneyv3.ui.movements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.WishLists;


import java.util.ArrayList;

public class WishListsTabFragment extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if(bundle != null){
            ArrayList<WishLists> confirmedWishLists = (ArrayList<WishLists>) bundle.getSerializable(Keys.SerializableKeys.WISH_LIST_KEYS);

            if (confirmedWishLists.size() > 0){
                // todo build expandable list
            } else {
                Toast.makeText(getContext(), "Non sono ancora state acquistate delle liste", Toast.LENGTH_LONG).show();
            }

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
