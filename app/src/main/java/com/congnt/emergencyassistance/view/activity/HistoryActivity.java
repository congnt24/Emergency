package com.congnt.emergencyassistance.view.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.utility.IntentUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.HistoryAdapter;
import com.congnt.emergencyassistance.entity.ItemHistory;
import com.congnt.emergencyassistance.entity.firebase.User;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

@Activity(mainLayoutId = R.layout.activity_history,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class HistoryActivity extends AwesomeActivity {
    private RecyclerView recycler;
    private HistoryAdapter adapter;
    private List<ItemHistory> listHistory = new ArrayList<>();

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
//        listHistory = MySharedPreferences.getInstance(this).listHistoty.load(new ArrayList<ItemHistory>());
        getFollowFromParse();
        recycler = (RecyclerView) mainView.findViewById(R.id.recycler_contact);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, listHistory, null);
        recycler.setAdapter(adapter);
    }

    public void getFollowFromParse(){
        listHistory.clear();
        User user = MySharedPreferences.getInstance(this).userProfile.load(null);
        ParseQuery<ParseFollow> query = ParseQuery.getQuery(ParseFollow.class);
        query.whereEqualTo("user", user.getEmail());
        query.findInBackground(new FindCallback<ParseFollow>() {
            @Override
            public void done(List<ParseFollow> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    listHistory.add(new ItemHistory(objects.get(i).getCreatedAt().toString(), "location", "type"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void finish() {
        //Save data
//        MySharedPreferences.getInstance(this).listContact.save(listContact);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
