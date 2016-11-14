package com.congnt.emergencyassistance.view.activity;

import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

@Activity(mainLayoutId = R.layout.activity_history,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class HistoryActivity extends AwesomeActivity {

    private static final int REQUEST_CONTACT_CODE = 1;
    private boolean toggleBoolean = true;
    private RecyclerView recycler;
    private HistoryAdapter adapter;
    private List<ItemHistory> listHistory;

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
        listHistory = MySharedPreferences.getInstance(this).listHistoty.load(new ArrayList<ItemHistory>());
        recycler = (RecyclerView) mainView.findViewById(R.id.recycler_contact);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new HistoryAdapter(this, null, null);
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_contact:
                startActivityForResult(IntentUtil.getContactIntent(), REQUEST_CONTACT_CODE);
                break;
            case R.id.action_remove_contact:
                toggleBoolean = !toggleBoolean;
                break;
        }
        return true;
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
