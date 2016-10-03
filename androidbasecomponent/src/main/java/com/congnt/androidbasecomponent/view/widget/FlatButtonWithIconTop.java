package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.R;

/**
 * Created by congnt24 on 03/10/2016.
 */

public class FlatButtonWithIconTop extends AwesomeLayout {
    private Drawable mIcon;
    private String mText;
    private ImageView iv_icon;
    private TextView tv_text;
    private RelativeLayout layout_parent;
    private int mTextColor;
    private String mTextTop;
    private TextView tv_text_top;

    public FlatButtonWithIconTop(Context context) {
        super(context);
    }

    public FlatButtonWithIconTop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FlatButtonWithIconTop,
                0, 0);
        try {
            mIcon = a.getDrawable(R.styleable.FlatButtonWithIconTop_flat_icon);
            mText = a.getString(R.styleable.FlatButtonWithIconTop_flat_text);
            mTextTop = a.getString(R.styleable.FlatButtonWithIconTop_flat_text_top);
            mTextColor = a.getInteger(R.styleable.FlatButtonWithIconTop_flat_text_color, Color.WHITE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.button_with_icon;
    }

    @Override
    protected void initAll(View rootView) {
        layout_parent = (RelativeLayout) rootView.findViewById(R.id.parent);
        iv_icon = (ImageView) rootView.findViewById(R.id.icon);
        tv_text = (TextView) rootView.findViewById(R.id.text);
        tv_text_top = (TextView) rootView.findViewById(R.id.text_top);
        if (mTextTop != null) {
            tv_text_top.setText(mTextTop);
        } else {
            tv_text_top.setVisibility(GONE);
        }
        tv_text.setTextColor(mTextColor);
        tv_text_top.setTextColor(mTextColor);
        if (mIcon != null) {
            iv_icon.setImageDrawable(mIcon);
        } else {
            iv_icon.setVisibility(GONE);
        }
        if (mText != null) {
            tv_text.setText(mText);
        } else {
            tv_text.setVisibility(GONE);
        }
    }

    public void setText(String text) {
        tv_text.setText(text);
    }

    public void setTextTop(String text) {
        tv_text_top.setText(text);
    }

    public void setIcon(int drawableId) {
        iv_icon.setImageResource(drawableId);
    }
}
