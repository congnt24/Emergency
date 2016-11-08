package com.congnt.emergencyassistance.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.view.activity.TutorialActivity;

public class TutorialFragment extends AwesomeFragment implements View.OnClickListener {
    private int descStringId;
    private int drawableId;
    private int titleStringId;
    private boolean isLast;

    public static AwesomeFragment newInstance(int drawableId, int titleStringId, int descStringId, boolean isLast) {
        TutorialFragment instance = new TutorialFragment();
        Bundle b = new Bundle();
        b.putInt("image", drawableId);
        b.putInt("title", titleStringId);
        b.putInt("desc", descStringId);
        b.putBoolean("last", isLast);
        instance.setArguments(b);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drawableId = getArguments().getInt("image");
            titleStringId = getArguments().getInt("title");
            descStringId = getArguments().getInt("desc");
            isLast = getArguments().getBoolean("last");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tutorial;
    }

    @Override
    protected void initAll(View rootView) {
        ImageView img = (ImageView) rootView.findViewById(R.id.iv_image);
        TextView title = (TextView) rootView.findViewById(R.id.tv_title);
        TextView desc = (TextView) rootView.findViewById(R.id.tv_desc);
        Button next = (Button) rootView.findViewById(R.id.btn_next);
        img.setImageResource(drawableId);
        title.setText(getString(titleStringId));
        desc.setText(getString(descStringId));
        next.setOnClickListener(this);
        if (isLast) {
            next.setText(getString(R.string.continue_to_app));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                ((TutorialActivity) getActivity()).switchFragment();
                break;
        }
    }
}
