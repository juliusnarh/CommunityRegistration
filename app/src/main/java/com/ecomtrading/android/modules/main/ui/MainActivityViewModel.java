package com.ecomtrading.android.modules.main.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.localstorage.DatabaseHelper;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<Community>> communityList;
    DatabaseHelper db;
    private final Application app;

    public MainActivityViewModel(@NonNull Application application) {
        app = application;
        communityList = new MutableLiveData<>();
        db = new DatabaseHelper(application);
        loadCommunityList();
    }

    public LiveData<List<Community>> getCommunityList() {
        db = new DatabaseHelper(app.getApplicationContext());
        loadCommunityList();
        if (communityList == null) {
            communityList = new MutableLiveData<>();
            loadCommunityList();
        }
        return communityList;
    }

    public void loadCommunityList() {
        communityList.setValue(db.getCommunityList());
    }
}
