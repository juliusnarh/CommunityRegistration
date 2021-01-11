package com.ecomtrading.android.data;

import com.google.gson.annotations.SerializedName;

public class GeoDistrict {
    public static final String TABLE_NAME = "geodistrict";

    public static final String COLUMN_DISTRICT_CODE = "districtcode";
    public static final String COLUMN_DISTRICT_NAME = "districtname";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_DISTRICT_CODE + " INTEGER PRIMARY KEY,"
                    + COLUMN_DISTRICT_NAME + " TEXT"
                    + ")";

    @SerializedName("Geo_District_Code") int districtcode;
    @SerializedName("Geo_District_Name") String districtname;

    public GeoDistrict() {
    }

    public GeoDistrict(int districtcode, String districtname) {
        this.districtcode = districtcode;
        this.districtname = districtname;
    }

    public int getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(int districtcode) {
        this.districtcode = districtcode;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }
}
