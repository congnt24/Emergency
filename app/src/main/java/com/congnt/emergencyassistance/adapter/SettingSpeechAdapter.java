package com.congnt.emergencyassistance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;

import java.util.List;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class SettingSpeechAdapter extends AwesomeRecyclerAdapter<SettingSpeechAdapter.ViewHolder, ItemSettingSpeech> {

    private boolean isEditMode;

    public SettingSpeechAdapter(Context context, List<ItemSettingSpeech> mList, OnClickListener<ItemSettingSpeech> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_setting_speech;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        ItemSettingSpeech item = mList.get(position);
        if (item != null) {
            holder.bindView(item);
        }
    }

    public void enableEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_command;
        ImageView iv_emergencyType;
        ImageView iv_speech_remove;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_command = (TextView) itemView.findViewById(R.id.item_setting_speech1);
            iv_emergencyType = (ImageView) itemView.findViewById(R.id.item_setting_speech2);
            iv_speech_remove = (ImageView) itemView.findViewById(R.id.item_speech_remove);
            iv_speech_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mList.remove(position);
                    notifyItemRemoved(position);
                    MySharedPreferences.getInstance(context).emergency_command.save(mList);
                }
            });
        }
        public void bindView(final ItemSettingSpeech item){
            if (isEditMode) {
                iv_speech_remove.setVisibility(View.VISIBLE);
            } else {
                iv_speech_remove.setVisibility(View.GONE);
            }
            tv_command.setText(item.getCommand());
            int resId = 0;
            switch (item.getEmergencyType()){
                case POLICE:
                    resId = R.drawable.police;
                    break;
                case FIRE:
                    resId = R.drawable.fire;
                    break;
                case AMBULANCE:
                    resId = R.drawable.ambulance;
                    break;
            }
            if (resId!=0) iv_emergencyType.setImageResource(resId);
        }
    }
}
