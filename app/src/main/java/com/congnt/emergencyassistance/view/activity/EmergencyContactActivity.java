package com.congnt.emergencyassistance.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.adapter.TouchHelperCallback;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.utility.ContactUtil;
import com.congnt.androidbasecomponent.utility.IntentUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.ContactAdapter;
import com.congnt.emergencyassistance.entity.ItemContact;

import java.util.ArrayList;
import java.util.List;

@Activity(mainLayoutId = R.layout.activity_emergency_contact,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class EmergencyContactActivity extends AwesomeActivity {

    private static final int REQUEST_CONTACT_CODE = 1;
    private RecyclerView recycler_contact;
    private ContactAdapter adapter;
    private List<ItemContact> listContact;
    private boolean toggleBoolean = true;

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
        listContact = MySharedPreferences.getInstance(this).listContact.load(new ArrayList<ItemContact>());
        recycler_contact = (RecyclerView) mainView.findViewById(R.id.recycler_contact);
        recycler_contact.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ContactAdapter(this, listContact, new AwesomeRecyclerAdapter.OnClickListener<ItemContact>() {
            @Override
            public void onClick(ItemContact item, int position) {
            }
        });
        recycler_contact.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new TouchHelperCallback(adapter, false, true);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler_contact);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CONTACT_CODE:
                    Uri uriContact = data.getData();
                    String contactName = ContactUtil.retrieveContactName(this, uriContact);
                    String contactNumber = ContactUtil.retrieveContactNumber(this, uriContact);
                    if (contactNumber == null) {
                        Toast.makeText(this, "Your contact not have phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        ItemContact item = new ItemContact(uriContact.toString(), contactName, contactNumber);
                        if (MySharedPreferences.getInstance(this).listContact.put(item)) {
                            listContact.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
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
                adapter.enableEditMode(toggleBoolean);
                toggleBoolean = !toggleBoolean;
                break;
        }
        return true;
    }

    @Override
    public void finish() {
        //Save data
        MySharedPreferences.getInstance(this).listContact.save(listContact);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
