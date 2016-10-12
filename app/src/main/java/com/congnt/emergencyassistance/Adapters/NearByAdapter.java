package com.congnt.emergencyassistance.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.FormatUtil;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.RetrofitPlaceEntity.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class NearByAdapter extends AwesomeRecyclerAdapter<NearByAdapter.ViewHolder, Result> {

    public NearByAdapter(Context context, List<Result> mList, OnClickListener<Result> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_nearby;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        Result item = mList.get(position);
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_place;
        TextView tv_location;
        TextView tv_distance;
        ImageView iv_gmap;
        ImageView iv_type;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            iv_gmap = (ImageView) itemView.findViewById(R.id.iv_gmap);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_type);

        }
        public void bindView(final Result item){
            tv_place.setText(item.getName());
            tv_location.setText(item.getVicinity());
            tv_distance.setText(FormatUtil.formatFloat(item.getDistance()/1000, 1)+" km");
            Picasso.with(itemView.getContext()).load(item.getIcon()).into(iv_type);
            iv_gmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommunicationUtil.dialTo(v.getContext(), "1234");
                }
            });
        }
    }
}
