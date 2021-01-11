package com.ecomtrading.android.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.data.GeoDistrict;
import com.ecomtrading.android.data.MasterData;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "community_registration_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MasterData.CREATE_TABLE);
        db.execSQL(GeoDistrict.CREATE_TABLE);
        db.execSQL(Community.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MasterData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GeoDistrict.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Community.TABLE_NAME);

        onCreate(db);
    }

    public long insertCommunity(Community community) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Community.COLUMN_COMMUNITY_NAME, community.getCommunityName());
        values.put(Community.COLUMN_GEOGRAPHICAL_DISTRICT, community.getGeoDistrict());
        values.put(Community.COLUMN_ACCESSIBILITY, community.getAccessibility());
        values.put(Community.COLUMN_DISTANCE_ECOM, community.getDistancecom());
        values.put(Community.COLUMN_DATE_OF_LICENSE, community.getConnectedecg());
        values.put(Community.COLUMN_LATITUDE, community.getLatitude());
        values.put(Community.COLUMN_LONGITUDE, community.getLongitude());
        values.put(Community.COLUMN_COMMUNITY_IMAGE, community.getImage());

        long id = db.insert(Community.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Community getCommunity(int localId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Community.TABLE_NAME,
                new String[]{Community.COLUMN_COMMUNITY_NAME, Community.COLUMN_COMMUNITY_ID, Community.COLUMN_GEOGRAPHICAL_DISTRICT, Community.COLUMN_ACCESSIBILITY, Community.COLUMN_CONNECTED_ECG, Community.COLUMN_DATE_OF_LICENSE, Community.COLUMN_DISTANCE_ECOM, Community.COLUMN_LATITUDE, Community.COLUMN_LONGITUDE, Community.COLUMN_COMMUNITY_IMAGE},
                Community.COLUMN_COMMUNITY_ID + "=?",
                new String[]{String.valueOf(localId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Community community = new Community(
                cursor.getString(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_NAME)),
                cursor.getInt(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_ID)),
                cursor.getInt(cursor.getColumnIndex(Community.COLUMN_GEOGRAPHICAL_DISTRICT)),
                cursor.getInt(cursor.getColumnIndex(Community.COLUMN_ACCESSIBILITY)),
                cursor.getString(cursor.getColumnIndex(Community.COLUMN_CONNECTED_ECG)),
                cursor.getString(cursor.getColumnIndex(Community.COLUMN_DATE_OF_LICENSE)),
                cursor.getInt(cursor.getColumnIndex(Community.COLUMN_DISTANCE_ECOM)),
                cursor.getDouble(cursor.getColumnIndex(Community.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(Community.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_IMAGE))
        );

        cursor.close();

        return community;
    }

    public List<Community> getCommunityList() {
        List<Community> communityList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Community.TABLE_NAME + " ORDER BY " +
                Community.COLUMN_DATE_OF_LICENSE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Community community = new Community();
                        community.setCommunityName(cursor.getString(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_NAME)));
                        community.setGeoDistrict(cursor.getInt(cursor.getColumnIndex(Community.COLUMN_GEOGRAPHICAL_DISTRICT)));
                        community.setAccessibility(cursor.getInt(cursor.getColumnIndex(Community.COLUMN_ACCESSIBILITY)));
                        community.setConnectedecg(cursor.getString(cursor.getColumnIndex(Community.COLUMN_CONNECTED_ECG)));
                        community.setDateoflicense(cursor.getString(cursor.getColumnIndex(Community.COLUMN_DATE_OF_LICENSE)));
                        community.setDistancecom(cursor.getInt(cursor.getColumnIndex(Community.COLUMN_DISTANCE_ECOM)));
                        community.setLatitude(cursor.getDouble(cursor.getColumnIndex(Community.COLUMN_LATITUDE)));
                        community.setLongitude(cursor.getDouble(cursor.getColumnIndex(Community.COLUMN_LONGITUDE)));
                        community.setLocalid(cursor.getInt(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_ID)));
                        community.setImage(cursor.getString(cursor.getColumnIndex(Community.COLUMN_COMMUNITY_IMAGE)));

                communityList.add(community);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return communityList;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Community.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Community community) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Community.COLUMN_COMMUNITY_NAME, community.getCommunityName());

        // updating row
        return db.update(Community.TABLE_NAME, values, Community.COLUMN_COMMUNITY_ID + " = ?",
                new String[]{String.valueOf(community.getLocalid())});
    }

    public void deleteNote(Community community) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Community.TABLE_NAME, Community.COLUMN_COMMUNITY_ID + " = ?",
                new String[]{String.valueOf(community.getLocalid())});
        db.close();
    }

    public List<GeoDistrict> getGeoDistrictList() {
        List<GeoDistrict> geoDistrictList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + GeoDistrict.TABLE_NAME + " ORDER BY " +
                GeoDistrict.COLUMN_DISTRICT_CODE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GeoDistrict geoDistrict = new GeoDistrict();
                geoDistrict.setDistrictcode(cursor.getInt(cursor.getColumnIndex(GeoDistrict.COLUMN_DISTRICT_CODE)));
                geoDistrict.setDistrictname(cursor.getString(cursor.getColumnIndex(GeoDistrict.COLUMN_DISTRICT_NAME)));

                geoDistrictList.add(geoDistrict);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return geoDistrictList;
    }

    public List<MasterData> getMasterDataList() {
        List<MasterData> masterDataList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + MasterData.TABLE_NAME + " ORDER BY " +
                MasterData.COLUMN_MST_CODE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MasterData masterData = new MasterData();
                masterData.setMstcode(cursor.getInt(cursor.getColumnIndex(MasterData.COLUMN_MST_CODE)));
                masterData.setMstType(cursor.getString(cursor.getColumnIndex(MasterData.COLUMN_MST_TYPE)));
                masterData.setMstDescription(cursor.getString(cursor.getColumnIndex(MasterData.COLUMN_DESCRIPTION)));

                masterDataList.add(masterData);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return masterDataList;
    }


}
