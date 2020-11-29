package com.news.goodlife.Data.Remote;

import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.news.goodlife.Interfaces.JsonCompanyNameApi;
import com.news.goodlife.Models.CompanyWebsite;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LookupCompanyLogo {

    ImageView iview;
    public LookupCompanyLogo(String name, ImageView iview) {




        String mystring = name;
        String arr[] = mystring.split(" ", 2);

        String firstWord = arr[0];   //the
        String theRest = arr[1];     //quick brown fox

        Log.i("Name", ""+firstWord);

        this.iview = iview;
        getWebsite(firstWord.toLowerCase(), iview);
    }

    public void getWebsite(String name, ImageView iview){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://company.clearbit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //.addConverterFactory(GsonConvert)

        JsonCompanyNameApi jsonCompanyNameApi = retrofit.create(JsonCompanyNameApi.class);

        String base = "sk_26bb9221d1ee01364aec820d67bec714:";
        String url = "v1/domains/find?name="+name;
        Call<CompanyWebsite> call = jsonCompanyNameApi.getCompanyNames("Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP), url) ;

        call.enqueue(new Callback<CompanyWebsite>() {
            @Override
            public void onResponse(Call<CompanyWebsite> call, Response<CompanyWebsite> response) {

                if(!response.isSuccessful()){
                    //Error accured
                    Log.i("JsonNot no success", ""+response.code());

                    return;
                }

                CompanyWebsite website = response.body();

                Log.i("JsonNot success", ""+website.getDomain());
                try{
                    Picasso.get().load(website.getLogo()).into(iview);
                }
                catch(Exception e){

                }


            }

            @Override
            public void onFailure(Call<CompanyWebsite> call, Throwable t) {

                Log.i("JsonException", ""+t.getMessage());

            }
        });
    }
}
