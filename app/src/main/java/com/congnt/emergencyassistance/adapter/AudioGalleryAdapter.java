package com.congnt.emergencyassistance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemAudio;

import java.util.List;

/**
 * Created by congnt24 on 12/12/2016.
 */

public class AudioGalleryAdapter extends AwesomeRecyclerAdapter<AudioGalleryAdapter.ViewHolder, ItemAudio> {
    public AudioGalleryAdapter(Context context, List<ItemAudio> mList, OnClickListener<ItemAudio> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_audio_gallery;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        ItemAudio item = mList.get(position);
        if (item != null) {
            holder.bindData(item);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_play;
        ImageView iv_pause;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            iv_pause = (ImageView) itemView.findViewById(R.id.iv_pause);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }

        public void bindData(ItemAudio s) {
            tv_name.setText(s.name);
            if (s.isPlay) {
                iv_pause.setVisibility(View.VISIBLE);
                iv_play.setVisibility(View.GONE);
            } else {
                iv_pause.setVisibility(View.GONE);
                iv_play.setVisibility(View.VISIBLE);
            }
        }
    }
}
