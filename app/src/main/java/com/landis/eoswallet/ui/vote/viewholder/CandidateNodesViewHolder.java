package com.landis.eoswallet.ui.vote.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemCandidateNodesBinding;
import com.landis.eoswallet.ui.vote.viewmodel.item.CandidateNodesItemViewModel;

public class CandidateNodesViewHolder extends BaseViewHolder<ItemCandidateNodesBinding, CandidateNodesItemViewModel> {


    public CandidateNodesViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_candidate_nodes);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new CandidateNodesItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

    }

    public void setContext(Context context){
        mViewModel.setContext(context);
    }
}
