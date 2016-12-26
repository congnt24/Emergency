package com.congnt.emergencyassistance.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.firebase.User;

@Activity(mainLayoutId = R.layout.activity_profile,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class ProfileActivity extends AwesomeActivity {

    private static final String TAG = ProfileActivity.class.toString();
    private LinearLayout activityProfile;
    private CardView layoutName;
    private TextView tvTitleName;
    private TextView tvValueName;
    private CardView layoutDate;
    private TextView tvTitleDate;
    private TextView tvValueDate;
    private CardView layoutGender;
    private TextView tvTitleGender;
    private TextView tvValueGender;
    private CardView layoutAddress;
    private TextView tvTitleAddress;
    private TextView tvValueAddress;
    private CardView layoutPhone;
    private TextView tvTitlePhone;
    private TextView tvValuePhone;
    private View.OnClickListener layoutOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_name:
                    createDialog(tvValueName, tvTitleName.getText().toString());
                    break;
                case R.id.layout_date:
                    createDialog(tvValueDate, tvTitleDate.getText().toString());
                    break;
                case R.id.layout_gender:
                    createDialog(tvValueGender, tvTitleGender.getText().toString());
                    break;
                case R.id.layout_address:
                    createDialog(tvValueAddress, tvTitleAddress.getText().toString());
                    break;
                case R.id.layout_phone:
                    createDialog(tvValuePhone, tvTitlePhone.getText().toString());
                    break;
            }
        }
    };

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
        //init data
        initViewFromXML();
        initListener();
        loadProfile();
    }

    private void createDialog(final TextView textView, String title) {
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);
        final EditText editText = (EditText) customView.findViewById(R.id.edit_text);
        editText.setText(textView.getText());
        editText.setSelection(editText.getText().length());
        DialogBuilder.customDialog(this, "Enter your " + title, customView, R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                textView.setText(editText.getText().toString());
            }
        }).create().show();
    }

    private void initListener() {
        layoutName.setOnClickListener(layoutOnclickListener);
        layoutDate.setOnClickListener(layoutOnclickListener);
        layoutGender.setOnClickListener(layoutOnclickListener);
        layoutAddress.setOnClickListener(layoutOnclickListener);
        layoutPhone.setOnClickListener(layoutOnclickListener);
    }

    private void initViewFromXML() {
        activityProfile = (LinearLayout) findViewById(R.id.activity_profile);
        layoutName = (CardView) findViewById(R.id.layout_name);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        tvValueName = (TextView) findViewById(R.id.tv_value_name);
        layoutDate = (CardView) findViewById(R.id.layout_date);
        tvTitleDate = (TextView) findViewById(R.id.tv_title_date);
        tvValueDate = (TextView) findViewById(R.id.tv_value_date);
        layoutGender = (CardView) findViewById(R.id.layout_gender);
        tvTitleGender = (TextView) findViewById(R.id.tv_title_gender);
        tvValueGender = (TextView) findViewById(R.id.tv_value_gender);
        layoutAddress = (CardView) findViewById(R.id.layout_address);
        tvTitleAddress = (TextView) findViewById(R.id.tv_title_address);
        tvValueAddress = (TextView) findViewById(R.id.tv_value_address);
        layoutPhone = (CardView) findViewById(R.id.layout_phone);
        tvTitlePhone = (TextView) findViewById(R.id.tv_title_phone);
        tvValuePhone = (TextView) findViewById(R.id.tv_value_phone);
    }

    public void loadProfile() {
        User user = MySharedPreferences.getInstance(this).userProfile.load(null);
        if (user != null) {
            Log.d(TAG, "loadProfile() called " + user.toString());
            tvValueName.setText(user.getName());
            tvValueDate.setText(user.getDate());
            tvValueGender.setText(user.getGender());
            tvValueAddress.setText(user.getAddress());
            tvValuePhone.setText(user.getPhone());
        } else {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            tvValueName.setText("");
            tvValueDate.setText("");
            tvValueGender.setText("");
            tvValueAddress.setText("");
            tvValuePhone.setText("");
        }
    }

    public void saveProfile(User user) {
        MySharedPreferences.getInstance(this).userProfile.save(user);
    }

    @Override
    public void finish() {
        saveProfile(
                new User(tvValueName.getText().toString(), tvValueDate.getText().toString()
                        , tvValueAddress.getText().toString(), tvValueGender.getText().toString(), tvValuePhone.getText().toString())
        );
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
