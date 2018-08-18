package com.can.uploadimage.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LENOVO on 26/10/2017.
 */

public class RetroClient {


//    private static final String ROOT_URL = "http://inssang.can.web.id/images/";
    private static final String ROOT_URL = "http://suci.can.web.id/images/img/";

    public  RetroClient(){
    }

    private static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

    }

    public static APIService getService(){
        return getRetrofitClient().create(APIService.class);
    }
}
