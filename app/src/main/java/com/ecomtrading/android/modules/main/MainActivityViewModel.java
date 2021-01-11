package com.ecomtrading.android.modules.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.localstorage.DatabaseHelper;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Community>> communityList;
    DatabaseHelper db = new DatabaseHelper(getApplication());

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Community>> getCommunityList() {
        if (communityList == null) {
            communityList = new MutableLiveData<>();
            loadCommunityList();
        }
        return communityList;
    }

    private void loadCommunityList() {
        communityList.setValue(db.getCommunityList());
    }
}
