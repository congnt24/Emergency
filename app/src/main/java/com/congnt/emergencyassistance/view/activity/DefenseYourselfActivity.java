package com.congnt.emergencyassistance.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.network.RetrofitBuilder;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.SelfDefenseAdapter;
import com.congnt.emergencyassistance.entity.SelfDefense;
import com.congnt.emergencyassistance.network.RetrofitApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Activity(mainLayoutId = R.layout.activity_self_defense,
        actionbarType = com.congnt.androidbasecomponent.annotation.Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class DefenseYourselfActivity extends AwesomeActivity {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private SelfDefenseAdapter adapter;
    private List<SelfDefense.ItemSelfDefense> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new MainActionBar(this);
    }

    @Override
    protected void initialize(View mainView) {
        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelfDefenseAdapter(this, list, new AwesomeRecyclerAdapter.OnClickListener<SelfDefense.ItemSelfDefense>() {
            @Override
            public void onClick(SelfDefense.ItemSelfDefense item, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        retrofit = RetrofitBuilder.getRetrofit(AppConfig.API_URL_BASE, null, 0, 0);

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        final Call<ResponseBody> seflDefenseApi = retrofitApi.getSelfDefense();
        seflDefenseApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        SelfDefense selfDefense = new Gson().fromJson(response.body().string(), SelfDefense.class);
                        list.clear();
                        list.addAll(selfDefense.data);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("AAAAa", "" + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
