package com.congnt.emergencyassistance.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.OnEventListener;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.ContactSelectAdapter;
import com.congnt.emergencyassistance.entity.ItemContact;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.SettingSpeech;
import com.congnt.emergencyassistance.util.CountryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 31/10/2016.
 */

public class DialogFollow extends DialogFragment {
    private static final String TAG = "DialogFollow";
    private View rootView;
    private EditText etContent;
//    private Spinner spTemplate;
    private RecyclerView recycler;
    private ContactSelectAdapter adapter;
    private List<ItemContact> listContact;
    private String location = "";
    private String[] templateMsg;
    private OnEventListener<Void> listener;

    public static SettingSpeech getSettingSpeech(Context context) {
        SettingSpeech setting = null;
        ItemCountryEmergencyNumber countryNumber = MySharedPreferences.getInstance(context).countryNumber.load(null);
        if (countryNumber != null) {
            setting = CountryUtil.getSettingSpeechByCountry(context, countryNumber.countryCode);
        }
        if (setting == null) {
            setting = CountryUtil.getSettingSpeechByCountry(context, "us");
        }
        return setting;
    }

    public EditText getEtContent() {
        return etContent;
    }

    public void setOnEventListener(OnEventListener<Void> listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            location = b.getString("location");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        rootView = inflater.inflate(R.layout.dialog_layout_follow, null);
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme2_SMSDialog);
        builder.setView(rootView);
        builder.setTitle("Send SMS")
                .setPositiveButton(getActivity().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) listener.onSuccess(null);
                        sendSMS();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) listener.onError(null);
                        dialog.dismiss();
                    }
                });
        return builder.show();
    }

    private void sendSMS() {
        String number = "";
        for (int i = 0; i < adapter.listSelected.size(); i++) {
            number += adapter.listSelected.get(i);
            if (i < adapter.listSelected.size() - 1) {
                number += "; ";
            }
        }

        String message = etContent.getText().toString() + "\n" + location;
        CommunicationUtil.sendSms(getActivity(), number, message);   //"5556; 5558; 5560"
    }

    private void initView() {
        etContent = (EditText) rootView.findViewById(R.id.et_content);
//        spTemplate = (Spinner) rootView.findViewById(R.id.sp_template);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler_contact);
        //Setup Spinner
        SettingSpeech setting = null;
        setting = getSettingSpeech(getActivity());
        int size = setting.templateMessage.size();
        templateMsg = new String[size];
        for (int i = 0; i < size; i++) {
            templateMsg[i] = setting.templateMessage.get(i).message;
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, templateMsg);
//         Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spTemplate.setAdapter(dataAdapter);
//        spTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                etContent.setText(templateMsg[position] + "\n");
//                etContent.setSelection(etContent.getText().length());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        //Recycler
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        listContact = MySharedPreferences.getInstance(getActivity()).listContact.load(new ArrayList<ItemContact>());
        if (listContact.size() > 6) listContact = listContact.subList(0, 5);
        adapter = new ContactSelectAdapter(getActivity(), listContact, null);
        recycler.setAdapter(adapter);
    }
}
