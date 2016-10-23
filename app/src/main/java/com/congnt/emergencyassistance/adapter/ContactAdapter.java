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
import com.congnt.emergencyassistance.entity.ItemContact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class ContactAdapter extends AwesomeRecyclerAdapter<ContactAdapter.ViewHolder, ItemContact> {

    private boolean isEditMode;

    public ContactAdapter(Context context, List<ItemContact> mList, OnClickListener<ItemContact> onClickListener) {
        super(context, mList, onClickListener);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_emergency_contacts;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
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
        ImageView ivRemove;
        ImageView ivContactPhotoNoImage;
        CircleImageView ivContactPhoto;
        TextView tvContactName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivRemove = (ImageView) itemView.findViewById(R.id.iv_contact_remove);
            ivContactPhoto = (CircleImageView) itemView.findViewById(R.id.iv_contact_photo);
            ivContactPhotoNoImage = (ImageView) itemView.findViewById(R.id.iv_contact_photo_no_image);
            tvContactName = (TextView) itemView.findViewById(R.id.tv_contact_name);
            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });

        }

        public void bindView(final ItemContact item) {
            if (isEditMode) {
                ivRemove.setVisibility(View.VISIBLE);
            } else {
                ivRemove.setVisibility(View.GONE);
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
