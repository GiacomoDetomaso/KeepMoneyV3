package com.example.keepmoneyv3.ui.wishlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WishListsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WishListsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Qui un giorno ci saranno le wishlist");
    }

    public LiveData<String> getText() {
        return mText;
    }
}