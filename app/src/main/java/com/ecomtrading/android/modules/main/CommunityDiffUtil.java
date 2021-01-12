package com.ecomtrading.android.modules.main;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.ecomtrading.android.data.Community;

import java.util.List;

public class CommunityDiffUtil extends DiffUtil.Callback{

    List<Community> oldCommunity;
    List<Community> newCommunity;

    public CommunityDiffUtil(List<Community> newCommunity, List<Community> oldCommunity) {
        this.newCommunity = newCommunity;
        this.oldCommunity = oldCommunity;
    }

    @Override
    public int getOldListSize() {
        return oldCommunity.size();
    }

    @Override
    public int getNewListSize() {
        return newCommunity.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCommunity.get(oldItemPosition).getLocalid() == newCommunity.get(newItemPosition).getLocalid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCommunity.get(oldItemPosition).equals(newCommunity.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}