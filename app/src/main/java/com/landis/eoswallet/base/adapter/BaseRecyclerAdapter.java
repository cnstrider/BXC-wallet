package com.landis.eoswallet.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract  class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>  {

    private static final int FOOT_ITEM = 9999;
    private int _resLayoutId;
    private int _footViewId;
    private List<T> _data = new ArrayList<>();
    private Context _context;

    public Context getContext() {
        return _context;
    }

    public List<T> getDatas() {
        return _data;
    }

    public T getData(int position) {
        if (position < 0 || _data == null || _data.size() == 0) {
            return null;
        }

        if (_data.size() - 1 < position) {
            return null;
        }
        return _data.get(position);
    }

    public BaseRecyclerAdapter() {
    }

    public BaseRecyclerAdapter(List<T> data, int resLayoutId) {
        _data = data;
        _resLayoutId = resLayoutId;
    }

    public BaseRecyclerAdapter(List<T> data, int resLayoutId, int footViewId) {
        _data = data;
        _resLayoutId = resLayoutId;
        _footViewId = footViewId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _context = parent.getContext();
        ViewDataBinding binding;
        if (viewType == FOOT_ITEM && _footViewId != 0) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), _footViewId, parent, false);
        } else {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), _resLayoutId, parent, false);
        }
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.BaseViewHolder holder, int position) {
        bindItem(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                if (itemClickListener != null) itemClickListener.onItemClick(position);
            });
        }

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else { //预留  未完善单item内的局部刷新
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        int addFootIndex = _footViewId == 0 ? 0 : 1;
        return _data == null ? addFootIndex : addFootIndex + _data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == _data.size()) {
            return FOOT_ITEM;
        }
        return super.getItemViewType(position);
    }

    protected abstract void bindItem(BaseViewHolder holder, int position);


    public void update(List<T> objects) {
        _data.clear();
        _data.addAll(objects);
        notifyDataSetChanged();
    }

    public void addItem(T object) {
        _data.add(object);
        notifyItemChanged(_data.size() - 1);
    }

    public void addAll(List<T> objects) {
        _data.addAll(objects);
        notifyDataSetChanged();
    }

    public void insertAll(List<T> objects) {
        _data.addAll(objects);
        notifyItemRangeChanged(getItemCount(), getItemCount() + objects.size());
    }

    public void insertItem(int positon, T object) {
        _data.add(positon, object);
        notifyItemChanged(positon);
    }

    public void updateItem(int positon, T object) {
        _data.set(positon, object);
        notifyItemChanged(positon);
    }

    public void deleteItem(int positon) {
        _data.remove(positon);
        notifyItemRemoved(positon);
    }

    public void clean() {
        _data = new ArrayList<>();
        notifyDataSetChanged();
    }


    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding binding;

        public BaseViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public BaseViewHolder(View v) {
            super(v);
            this.binding = null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
