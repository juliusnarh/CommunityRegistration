package com.ecomtrading.android.modules.main.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecomtrading.android.R;
import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.databinding.ItemCommuntiyBinding;
import com.ecomtrading.android.localstorage.DatabaseHelper;
import com.ecomtrading.android.modules.main.CommunityDiffUtil;
import com.ecomtrading.android.modules.registration.RegisterActivity;
import com.ecomtrading.android.util.AndroidUtils;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {
    private ArrayList<Community> communityList = new ArrayList<>();
    private Context context;
    private String id = "";
    MainActivity activity;
    ItemCommuntiyBinding binding;
    FragmentManager fm;
    DatabaseHelper databaseHelper;

    public CommunityAdapter(Context context, FragmentManager fm, AppCompatActivity activity) {
        this.context = context;
        this.fm = fm;
        this.activity = (MainActivity) activity;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_communtiy, viewGroup, false);
        return new CommunityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int i) {
        Community community = communityList.get(i);
        holder.bind(community);
        binding.ripple.setOnClickListener(v -> {
            showPopupMenu(holder.itemView, community.getLocalid(), holder.getAdapterPosition());
        });
        binding.mapViewButton.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return communityList != null ? communityList.size() : 0;
    }

    public void setCommunityList(ArrayList<Community> communityList) {
        this.communityList = communityList;
    }

    public void updateList(ArrayList<Community> newList) {
        this.communityList = newList;
        notifyDataSetChanged();
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommunityDiffUtil(this.communityList, newList));
//        diffResult.dispatchUpdatesTo(this);
    }

    static class CommunityViewHolder extends RecyclerView.ViewHolder {
        private ItemCommuntiyBinding binding;

        public CommunityViewHolder(@NonNull ItemCommuntiyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Community community) {
            binding.accessibility.setText(String.valueOf(community.getAccessibility()));
            binding.geographicalDistrict.setText(String.valueOf(community.getGeoDistrict()));
            binding.communityName.setText(community.getCommunityName());
            binding.distanceEcom.setText(String.valueOf(community.getDistancecom()));
            binding.connectedEcg.setText(community.getConnectedecg());
            binding.licenseDate.setText(community.getDateoflicense());
            binding.latitude.setText(String.valueOf(community.getLatitude()));
            binding.longitude.setText(String.valueOf(community.getLongitude()));
            binding.photo.setImageBitmap(AndroidUtils.base64ToBitmap(community.getImage()));

            binding.executePendingBindings();
        }
    }

    private void showPopupMenu(View view, int localid, int position) {
        // inflate menu
        id = String.valueOf(localid);
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_community_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupClickListener());
        popup.show();
    }

    class PopupClickListener implements PopupMenu.OnMenuItemClickListener {

        public PopupClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    EditCommunityFragment.newInstance(id).show(fm, "Dialog Fragment");
                    return true;
                case R.id.action_delete:
                    databaseHelper.deleteCommunity(id);
                    activity.refreshList();
                    return true;
                default:
            }
            return false;
        }
    }

//    private void openDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Delete")
//                .setMessage("Are you sure you want to delete?")
//                .setPositiveButton("yes", (dialog, which) -> {
//
//                })
//                .setNegativeButton("no", (dialog, which) -> {
//                    dialog.dismiss();
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

}