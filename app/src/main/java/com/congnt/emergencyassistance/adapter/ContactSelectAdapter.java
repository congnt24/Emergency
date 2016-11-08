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
import com.congnt.androidbasecomponent.utility.AnimationUtil;
import com.congnt.androidbasecomponent.utility.ContactUtil;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemContact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class ContactSelectAdapter extends AwesomeRecyclerAdapter<ContactSelectAdapter.ViewHolder, ItemContact> {

    public List<String> listSelected = new ArrayList<>();
    private boolean isEditMode;

    public ContactSelectAdapter(Context context, List<ItemContact> mList, OnClickListener<ItemContact> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_select_contacts;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindHolder(holder, position);
    }

    @Override
    protected void bindHolder(ViewHolder holder, int position) {
        ItemContact item = mList.get(holder.getAdapterPosition());
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void enableEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelect;
        ImageView ivContactPhotoNoImage;
        CircleImageView ivContactPhoto;
        TextView tvContactName;
        boolean isSelect = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSelect = (ImageView) itemView.findViewById(R.id.iv_select);
            ivContactPhoto = (CircleImageView) itemView.findViewById(R.id.iv_contact_photo);
            ivContactPhotoNoImage = (ImageView) itemView.findViewById(R.id.iv_contact_photo_no_image);
            tvContactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelect = !isSelect;
                    int position = getAdapterPosition();
                    if (isSelect) {
                        AnimationUtil.fadeInView(ivSelect);
                        listSelected.add(mList.get(position).getContactNumber());
                    } else {
                        AnimationUtil.fadeOutView(ivSelect);
                        listSelected.remove(mList.get(position).getContactNumber());
                    }
                }
            });

        }

        public void bindView(final ItemContact item) {
            if (isEditMode) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.GONE);
            }
            tvContactName.setText(item.getContactName());
            Bitmap contactPhoto = ContactUtil.retrieveContactPhoto(context, item.getUriContact());
            if (contactPhoto != null) {
                ivContactPhotoNoImage.setVisibility(View.GONE);
                ivContactPhoto.setVisibility(View.VISIBLE);
                ivContactPhoto.setImageBitmap(contactPhoto);
            } else {
                ivContactPhotoNoImage.setVisibility(View.VISIBLE);
                ivContactPhoto.setVisibility(View.GONE);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(item.getContactName().toUpperCase().charAt(0) + "", Color.RED);
                ivContactPhotoNoImage.setImageDrawable(drawable);
            }
        }
    }

}
