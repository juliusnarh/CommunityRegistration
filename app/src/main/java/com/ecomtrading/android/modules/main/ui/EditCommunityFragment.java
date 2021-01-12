package com.ecomtrading.android.modules.main.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ecomtrading.android.R;
import com.ecomtrading.android.data.AccessToken;
import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.data.CommunityPojo;
import com.ecomtrading.android.data.GeoDistrict;
import com.ecomtrading.android.data.MasterData;
import com.ecomtrading.android.data.SaveCommunityResponse;
import com.ecomtrading.android.databinding.FragmentEditCommunityBinding;
import com.ecomtrading.android.localstorage.DatabaseHelper;
import com.ecomtrading.android.remotestorage.ApiClient;
import com.ecomtrading.android.remotestorage.ApiService;
import com.ecomtrading.android.util.AndroidUtils;
import com.ecomtrading.android.util.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.ilhasoft.support.validation.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;

import static android.app.Activity.RESULT_OK;
import static com.ecomtrading.android.util.AndroidUtils.bitmapToBase64;

public class EditCommunityFragment extends DialogFragment{
    static final int REQUEST_CAMERA = 100;
    public static String TAG = EditCommunityFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private String localID = "";
    private String imageString = "";
    private Community community = new Community();
    private Validator validator;
    private CommunityPojo communityPojo = new CommunityPojo();
    FragmentEditCommunityBinding binding;
    private ApiService apiService;
    private List<String> geoDistrict = new ArrayList<>();
    private List<String> geoDistrictTemp = new ArrayList<>();
    private List<String> masterDataAccessibility = new ArrayList<>();
    private List<String> masterDataAccessibilityTemp = new ArrayList<>();
    private List<String> masterDataEcomDistance = new ArrayList<>();
    private List<String> masterDataEcomDistanceTemp = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    private AlertDialog dialogAccessibility;
    private AlertDialog dialogECOMDistance;
    private AlertDialog dialogGeo;
    private String token;
    private Boolean geoBool = false;
    private Boolean accessBool = false;
    private Boolean ecomBool = false;
    private MaterialDialog prog;
    private Intent locintent;
    private DatabaseHelper db;
    public EditCommunityFragment() {
    }

    public static EditCommunityFragment newInstance(String param1) {
        EditCommunityFragment fragment = new EditCommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            localID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_community, container, false);
        validator = new Validator(binding);
        db = new DatabaseHelper(getActivity());
        binding.setCommunity(setCommunityData(db.getCommunity(localID)));
        binding.communityPhoto.setImageBitmap(AndroidUtils.base64ToBitmap(imageString));
        getDialog().setTitle("Edit Community Information");
        setupClickListener();

        setupApiService();
        getTokenFromServer();
        prepareDatePicker();
        return binding.getRoot();
    }
    private void getTokenFromServer() {
        Call<AccessToken> call = apiService.getAccessToken("murali", "welcome", "password");
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Token Access successful");
                    AccessToken accessToken = response.body();
                    token = accessToken.getTokenType() + " " + accessToken.getAccessToken();
                    getMasterDataCommonFromServer();
                    getMasterDataGeoDataFromServer();
                } else {
                    Log.e(TAG, "Token unsuccessful: onResponse");
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.d(TAG, "Access Token error: onFailure :" + t.getMessage());
            }
        });
    }

    private void getMasterDataCommonFromServer() {
        Call<List<MasterData>> call = apiService.getMasterDataCommon(token);
        call.enqueue(new Callback<List<MasterData>>() {
            @Override
            public void onResponse(Call<List<MasterData>> call, Response<List<MasterData>> response) {
                if (response.isSuccessful()) {
                    createCommonDialog(response.body());
                    prepareAccessibiltyDialog(binding.accessibility);
                    prepareECOMDistanceDialog(binding.distanceEcom);
                } else {
                    Log.e(TAG, "Master Data Common unsuccessful: onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<MasterData>> call, Throwable t) {
                Log.d(TAG, "Master Data Common error: onFailure :" + t.getMessage());

            }
        });
    }

    private void getMasterDataGeoDataFromServer() {
        Call<List<GeoDistrict>> call = apiService.getMasterDataGeoDistrict(token);
        call.enqueue(new Callback<List<GeoDistrict>>() {
            @Override
            public void onResponse(Call<List<GeoDistrict>> call, Response<List<GeoDistrict>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        GeoDistrict item = response.body().get(i);
                        geoDistrict.add(item.getDistrictcode() + " " + item.getDistrictname());
                        geoDistrictTemp.add(String.valueOf(item.getDistrictcode()));
                    }
                    prepareGeoDialog(binding.geographicalDistrict);
                } else {
                    Log.e(TAG, "Master Data geo data unsuccessful: onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<GeoDistrict>> call, Throwable t) {
                Log.d(TAG, "Master Data geo dataerror: onFailure :" + t.getMessage());

            }
        });
    }

    private void createCommonDialog(List<MasterData> masterDataList) {

        for (int i = 0; i < masterDataList.size(); i++) {
            MasterData masterData = masterDataList.get(i);
            if (masterData.getMstType().equalsIgnoreCase(Constants.ECOM_DISTANCE)) {
                masterDataEcomDistance.add(masterData.getMstcode() + " "+ masterData.getMstDescription());
                masterDataEcomDistanceTemp.add(String.valueOf(masterData.getMstcode()));
            } else
                masterDataAccessibility.add(masterData.getMstcode() + " "+ masterData.getMstDescription());
            masterDataAccessibilityTemp.add(String.valueOf(masterData.getMstcode()));
        }


    }

    private void setupClickListener() {
        binding.submitButton.setOnClickListener(v -> updateCommunity());
        binding.photoHeader.setOnClickListener(v -> dispatchTakePictureIntent());
        binding.connectedEcg.setOnClickListener(this::openConnectedToEcgDialog);
        binding.licenseDate.setOnClickListener(v -> openDatePicker());
        binding.distanceEcom.setOnClickListener(v -> {
            if (ecomBool) dialogECOMDistance.show();
        });
        binding.accessibility.setOnClickListener(v -> {
            if(accessBool) dialogAccessibility.show();
        });
        binding.geographicalDistrict.setOnClickListener(v -> {
            if(geoBool)dialogGeo.show();
        });
    }

    private void setupApiService() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private void prepareDatePicker() {
        calendar = Calendar.getInstance();
        date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateUI();
        };
    }

    private void updateDateUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        binding.licenseDate.setText(sdf.format(calendar.getTime()));
    }

    //Do two databinding
    public void openConnectedToEcgDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.label_connected_ecg)
                .setItems(R.array.connected_ecg, (dialog, which) -> {
                    if (which == 0) {
                        community.setConnectedecg("Y");
                        binding.connectedEcg.setText("Y");
                    } else {
                        community.setConnectedecg("N");
                        binding.connectedEcg.setText("N");

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void prepareECOMDistanceDialog(View view) {
        CharSequence[] cs = masterDataEcomDistance.toArray(new CharSequence[masterDataEcomDistance.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.label_distance_ecom)
                .setItems(cs, (dialog, which) -> {
                    binding.distanceEcom.setText(masterDataEcomDistanceTemp.get(which));
                });

        dialogECOMDistance = builder.create();
        ecomBool = true;
    }

    public void prepareAccessibiltyDialog(View view) {
        CharSequence[] cs = masterDataAccessibility.toArray(new CharSequence[masterDataAccessibility.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.label_accessibility)
                .setItems(cs, (dialog, which) -> {
                    binding.accessibility.setText(masterDataAccessibilityTemp.get(which));

                });

        dialogAccessibility = builder.create();
        accessBool = true;
    }



    public void prepareGeoDialog(View view) {
        CharSequence[] cs = geoDistrict.toArray(new CharSequence[geoDistrict.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.label_geographical)
                .setItems(cs, (dialog, which) -> {
                    binding.geographicalDistrict.setText(geoDistrictTemp.get(which));
                });

        dialogGeo = builder.create();
        geoBool=true;
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDatePicker() {
        new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.communityPhoto.setImageBitmap(imageBitmap);
            imageString = bitmapToBase64(imageBitmap);
        }
    }

    private CommunityPojo setCommunityData(Community community) {
        imageString = community.getImage();
        communityPojo.accessibility.set(String.valueOf(community.getAccessibility()));
        communityPojo.distancecom.set(String.valueOf(community.getDistancecom()));
        communityPojo.geoDistrict.set(String.valueOf(community.getGeoDistrict()));
        communityPojo.latitude.set(String.valueOf(community.getLatitude()));
        communityPojo.longitude.set(String.valueOf(community.getLongitude()));
        communityPojo.communityName.set(community.getCommunityName());
        communityPojo.connectedecg.set(community.getConnectedecg());
        communityPojo.dateoflicense.set(community.getDateoflicense());
        return communityPojo;
    }

    public void updateCommunity() {
        community.setImage(imageString);
        if (validator.validate() && !imageString.isEmpty()) {
            community.setLongitude(Double.parseDouble(binding.longitude.getText().toString()));
            community.setLatitude(Double.parseDouble(binding.latitude.getText().toString()));
            community.setConnectedecg(binding.connectedEcg.getText().toString());
            community.setCommunityName(binding.communityName.getText().toString());
            community.setGeoDistrict(Integer.parseInt(binding.geographicalDistrict.getText().toString()));
            community.setAccessibility(Integer.parseInt(binding.accessibility.getText().toString()));
            community.setDistancecom(Integer.parseInt(binding.distanceEcom.getText().toString()));
            community.setDateoflicense(binding.licenseDate.getText().toString());
            db.updateCommunity(community);
            Call<AccessToken> call = apiService.getAccessToken("murali", "welcome", "password");
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Token Access successful");
                        AccessToken accessToken = response.body();
                        token = accessToken.getTokenType() + " " + accessToken.getAccessToken();

                        Call<SaveCommunityResponse> callx = apiService.postCommunityData(token, community);
                        callx.enqueue(new Callback<SaveCommunityResponse>() {
                            @Override
                            public void onResponse(Call<SaveCommunityResponse> call, Response<SaveCommunityResponse> response) {
                                if (response.isSuccessful()) {
                                    SaveCommunityResponse communityResponse = response.body();
                                    if (communityResponse.getStatusCode() == 0){
//                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                        MotionToast.Companion.darkToast(getActivity(),
                                                "Success",
                                                "Upload Completed successfully!",
                                                MotionToast.TOAST_SUCCESS,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(getContext(),R.font.helvetica_regular));
                                        getDialog().dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                        AndroidUtils.writeToFile(new Gson().toJson(community), AndroidUtils.getUUID());
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "submit unsuccessful: onResponse");
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveCommunityResponse> call, Throwable t) {
                                Log.d(TAG, "Submit error: onFailure :" + t.getMessage());

                            }

                        });
                    } else {
                        Log.e(TAG, "Token unsuccessful: onResponse");
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.d(TAG, "Access Token error: onFailure :" + t.getMessage());
                }
            });
        } else {
//            binding.progressBar.setVisibility(View.GONE);
            MotionToast.Companion.darkToast(getActivity(),
                    "Error",
                    "All fields required",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getContext(),R.font.helvetica_regular));
        }


    }
}