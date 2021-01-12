package com.ecomtrading.android.modules.main.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecomtrading.android.R;
import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.databinding.ActivityMainBinding;
import com.ecomtrading.android.modules.registration.RegisterActivity;
import com.ecomtrading.android.util.AndroidUtils;
import com.ecomtrading.android.util.Constants;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding = null;
    private static final String TAG = MainActivity.class.getSimpleName();
    private AccountHeader headerResult = null;
    private MainActivityViewModel viewModel;
    private Drawer result = null;
    private CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupToolbar();
        setupRecyclerViewAndAdapter();
        setupViewModel();
        setupClickListeners();
        setupNavigationDrawer(savedInstanceState);
    }

    private void setupClickListeners() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication())).get(MainActivityViewModel.class);
        viewModel.getCommunityList().observe(this, communities -> {
            adapter.updateList((ArrayList<Community>) communities);
            hideEmptyCommunityList();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateList((ArrayList<Community>) viewModel.getCommunityList().getValue());
        hideEmptyCommunityList();
    }

    public void refreshList() {
        adapter.updateList((ArrayList<Community>) viewModel.getCommunityList().getValue());
        hideEmptyCommunityList();
    }

    public void hideEmptyCommunityList() {
        if (adapter.getItemCount() == 0) {
            binding.animationView.setVisibility(View.VISIBLE);
            binding.swipeContainer.setVisibility(View.GONE);
        } else {
            binding.animationView.setVisibility(View.GONE);
            binding.swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerViewAndAdapter() {
        adapter = new CommunityAdapter(getApplicationContext(), getSupportFragmentManager(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Dashboard");
    }

    private void setupNavigationDrawer(Bundle savedInstanceState) {
        final IProfile profile = new ProfileDrawerItem()
                .withName(Constants.AGENT_NAME)
                .withEmail(Constants.AGENT_ID)
                .withIcon("https://avatars2.githubusercontent.com/u/3597376?v=3&s=460").withIdentifier(100);


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.background_drawer)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.register_community).withIcon(FontAwesome.Icon.faw_pen_alt).withDescription(R.string.register_community_description).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.mapview).withIcon(FontAwesome.Icon.faw_map_marked).withDescription(R.string.map_view_description).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.sync).withIcon(FontAwesome.Icon.faw_sync).withDescription(R.string.sync_description).withIdentifier(2).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.colorPrimary)),
                        new SectionDrawerItem().withName("Send us you feedbacks"),
                        new SecondaryDrawerItem().withName("E-mail").withIcon(FontAwesome.Icon.faw_envelope).withIdentifier(200).withSelectable(false),
                        new SecondaryDrawerItem().withName("Visit Our Website").withIcon(GoogleMaterial.Icon.gmd_web).withIdentifier(201).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainActivity.this, RegisterActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
//                                intent = new Intent(MainActivity.this, MapViewActivitry.class);
                            } else if (drawerItem.getIdentifier() == 20) {
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/email");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"narhjuliuscarl@gmail.com"});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                            } else if (drawerItem.getIdentifier() == 21) {
                                String url = "http://ecomstrading.com";
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                            }

                            if (intent != null) {
                                startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(9, new StringHolder(unsyncedFiles() + " File(s)"));
    }

    public long unsyncedFiles() {
        try {
            return new File(AndroidUtils.uploadDirectoryPath()).listFiles().length;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return 0;
    }
}