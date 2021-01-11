package com.ecomtrading.android.data;

import com.google.gson.annotations.SerializedName;

public class MasterData {
    public static final String TABLE_NAME = "masterdata";

    public static final String COLUMN_MST_CODE = "mstcode";
    public static final String COLUMN_MST_TYPE = "msttype";
    public static final String COLUMN_DESCRIPTION = "mstdescription";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_MST_CODE + " INTEGER PRIMARY KEY,"
                    + COLUMN_MST_TYPE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT"
                    + ")";

    @SerializedName("Mst_Code") int mstcode;
    @SerializedName("Mst_Type") String mstType;
    @SerializedName("Mst_Desc") String mstDescription;

    public MasterData() {
    }

    public MasterData(int mstcode, String mstType, String mstDescription) {
        this.mstcode = mstcode;
        this.mstType = mstType;
        this.mstDescription = mstDescription;
    }

    public int getMstcode() {
        return mstcode;
    }

    public void setMstcode(int mstcode) {
        this.mstcode = mstcode;
    }

    public String getMstType() {
        return mstType;
    }

    public void setMstType(String mstType) {
        this.mstType = mstType;
    }

    public String getMstDescription() {
        return mstDescription;
    }

    public void setMstDescription(String mstDescription) {
        this.mstDescription = mstDescription;
    }
}
