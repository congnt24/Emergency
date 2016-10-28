package com.congnt.emergencyassistance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.SelfDefense;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class SelfDefenseAdapter extends AwesomeRecyclerAdapter<SelfDefenseAdapter.ViewHolder, SelfDefense.ItemSelfDefense> {

    public SelfDefenseAdapter(Context context, List<SelfDefense.ItemSelfDefense> mList, OnClickListener<SelfDefense.ItemSelfDefense> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_self_defense;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        SelfDefense.ItemSelfDefense item = mList.get(holder.getAdapterPosition());
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        public void bindView(final SelfDefense.ItemSelfDefense item) {
            title.setText(item.title);
            Picasso.with(context).load(item.img).into(image);
        }
    }

}
