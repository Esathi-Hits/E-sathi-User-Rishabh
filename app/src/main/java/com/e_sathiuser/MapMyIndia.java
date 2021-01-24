package com.e_sathiuser;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mmi.services.account.MapmyIndiaAccountManager;

public class MapMyIndia extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());
        MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());
        MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType(getAtlasGrantType());
        MapmyIndia.getInstance(this);
    }
    public String getAtlasClientId() {
        return "33OkryzDZsLtn9LbczSjvKIkAxTDoDRA5VRrijBq_Vbj4Ea7HJWH8E512twpoCRO85Prv-7VElAYrcs-Q694C3tEPDvtYCWWUtXrFRsHyw7XSnX3wI558w==";
    }
    public String getAtlasClientSecret() {
        return "lrFxI-iSEg9BnfnFk7r1uadVB_XovbocXkabIjRnDCEWYk7c1Z9epN1OqdC0ztzPIChSY0LotvYVGJM8nUVtRuT4eExdWtbPEbA6Ucam3dFr_FzeoVdscIIjMBF_pFS9";
    }
    public String getAtlasGrantType() {
        return "client_credentials";
    }

    public String getMapSDKKey() {
        return "utvp7uao37ym6grq78h88ukke5fx6ylc";
    }

    public String getRestAPIKey() {
        return "5ua8j2mliwxbky5tlr5epnec189j81ko";
    }
}