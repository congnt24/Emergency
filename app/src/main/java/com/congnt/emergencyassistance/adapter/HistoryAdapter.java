package com.congnt.emergencyassistance.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.utility.ContactUtil;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemHistory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class HistoryAdapter extends AwesomeRecyclerAdapter<HistoryAdapter.ViewHolder, ItemHistory> {


    public HistoryAdapter(Context context, List<ItemHistory> mList, OnClickListener<ItemHistory> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_history;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        ItemHistory item = mList.get(holder.getAdapterPosition());
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_history_location;
        public TextView item_history_time;
        public ViewHolder(View itemView) {
            super(itemView);
            item_history_location = (TextView) itemView.findViewById(R.id.item_history_location);
            item_history_time = (TextView) itemView.findViewById(R.id.item_history_time);
        }

        public void bindView(final ItemHistory item) {
            item_history_location.setText(item.getLocation());
            item_history_time.setText(item.getTime());
        }
    }

}
