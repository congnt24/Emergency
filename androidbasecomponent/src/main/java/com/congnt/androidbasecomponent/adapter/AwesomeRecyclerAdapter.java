package com.congnt.androidbasecomponent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by congn_000 on 9/14/2016.
 */
public abstract class AwesomeRecyclerAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    protected OnClickListener<T> onClickListener;
    protected List<T> mList;
    protected Context context;
    private LayoutInflater mLayoutInflater;

    public AwesomeRecyclerAdapter(Context context, List<T> mList, OnClickListener<T> onClickListener) {
        this.onClickListener = onClickListener;
        this.context = context;
        this.mList = mList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(getItemLayoutId(), parent, false);
        return getViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        bindHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onClick(mList.get(position), position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return onItemLongClick(holder, mList.get(position), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract int getItemLayoutId();

    protected abstract VH getViewHolder(View itemView);

    protected abstract void bindHolder(VH holder, int position);

    protected boolean onItemLongClick(VH holder, T t, int position) {
        return false;
    }

    ;

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    ;

    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    ;

    public interface OnClickListener<T> {
        void onClick(T item, int position);
    }
}
