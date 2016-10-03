package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by congn on 8/19/2016.
 */
public abstract class AwesomeLayout extends RelativeLayout {
    public AwesomeLayout(Context context) {
        super(context);
        init();
    }

    public AwesomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
        init();
    }

    public AwesomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AwesomeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttribute(context, attrs);
        init();
    }


    protected abstract int getLayoutId();

    protected abstract void initAll(View rootView);

    protected void initAttribute(Context context, AttributeSet attrs) {

    }

    private void init() {
        View rootView = View.inflate(getContext(), getLayoutId(), this);
        initAll(rootView);
    }
}
