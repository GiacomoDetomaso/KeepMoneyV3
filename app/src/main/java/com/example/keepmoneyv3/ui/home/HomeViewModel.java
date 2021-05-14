package com.example.keepmoneyv3.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Qui un giorno ci sarà lo storico movimenti");
    }

    public LiveData<String> getText() {
        return mText;
    }
}