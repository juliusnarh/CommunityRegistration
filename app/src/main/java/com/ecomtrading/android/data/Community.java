
package com.ecomtrading.android.data;


import com.google.gson.annotations.SerializedName;

public class Community {
    public static final String TABLE_NAME = "community";

    public static final String COLUMN_COMMUNITY_NAME = "communityname";
    public static final String COLUMN_COMMUNITY_ID = "localid";
    public static final String COLUMN_GEOGRAPHICAL_DISTRICT = "geodistrict";
    public static final String COLUMN_ACCESSIBILITY = "accessibility";
    public static final String COLUMN_DISTANCE_ECOM = "distanceecom";
    public static final String COLUMN_CONNECTED_ECG = "connectedecg";
    public static final String COLUMN_DATE_OF_LICENSE = "dateoflicense";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_COMMUNITY_IMAGE = "image";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_COMMUNITY_NAME + " TEXT,"
                    + COLUMN_COMMUNITY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COMMUNITY_NAME + " TEXT,"
                    + COLUMN_GEOGRAPHICAL_DISTRICT + " INTEGER,"
                    + COLUMN_ACCESSIBILITY + " INTEGER,"
                    + COLUMN_CONNECTED_ECG + " TEXT(1),"
                    + COLUMN_DATE_OF_LICENSE + " DATETIME,"
                    + COLUMN_DISTANCE_ECOM + " INTEGER,"
                    + COLUMN_LATITUDE + " DOUBLE,"
                    + COLUMN_LONGITUDE + " DOUBLE,"
                    + COLUMN_COMMUNITY_IMAGE + " TEXT"
                    + ")";

    @SerializedName("communityName") String communityName;
    @SerializedName("communityName") int localid;
    @SerializedName("geographicalDistrict") int geoDistrict;
    @SerializedName("accessibility") int accessibility;
    @SerializedName("connectedToECG") String connectedecg;
    @SerializedName("licenseDate") String dateoflicense;
    @SerializedName("distanceToECOM") int distancecom;
    @SerializedName("latitude") Double latitude;
    @SerializedName("longitude") Double longitude;
    @SerializedName("photo") String image;

    public Community() {
    }

    public Community(String communityName, int localid, int geoDistrict, int accessibility, String connectedecg, String dateoflicense, int distancecom, Double latitude, Double longitude, String image) {
        this.communityName = communityName;
        this.geoDistrict = geoDistrict;
        this.localid = localid;
        this.accessibility = accessibility;
        this.connectedecg = connectedecg;
        this.dateoflicense = dateoflicense;
        this.distancecom = distancecom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public int getLocalid() {
        return localid;
    }

    public void setLocalid(int localid) {
        this.localid = localid;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getGeoDistrict() {
        return geoDistrict;
    }

    public void setGeoDistrict(int geoDistrict) {
        this.geoDistrict = geoDistrict;
    }

    public int getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(int accessibility) {
        this.accessibility = accessibility;
    }

    public int getDistancecom() {
        return distancecom;
    }

    public void setDistancecom(int distancecom) {
        this.distancecom = distancecom;
    }

    public String getConnectedecg() {
        return connectedecg;
    }

    public void setConnectedecg(String connectedecg) {
        this.connectedecg = connectedecg;
    }

    public String getDateoflicense() {
        return dateoflicense;
    }

    public void setDateoflicense(String dateoflicense) {
        this.dateoflicense = dateoflicense;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
