package com.landis.eoswallet.ui.ringsign.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.viewholder.BaseViewHolder;
import com.landis.eoswallet.databinding.ItemMyProposeBinding;
import com.landis.eoswallet.net.model.ProposalBean;
import com.landis.eoswallet.net.model.ProposalInfo;
import com.landis.eoswallet.ui.ringsign.listener.DeteleListener;
import com.landis.eoswallet.ui.ringsign.viewmodel.item.ProposalInfoItemViewModel;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.litepal.LitePal;

import java.util.List;

public class ProposalInfoViewHolder extends BaseViewHolder<ItemMyProposeBinding, ProposalInfoItemViewModel> {

    private DeteleListener deteleListener;

    public ProposalInfoViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_my_propose);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ProposalInfoItemViewModel();
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void init() {

        mDataBinding.tvProposeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProposalInfo.RowsBean rowsBean = mViewModel.getBaseItemModel();
                List<ProposalBean> proposalBeans = LitePal.findAll(ProposalBean.class);
                ((SwipeMenuLayout) mDataBinding.getRoot()).quickClose();
                for (ProposalBean proposalBean : proposalBeans) {
                    if (proposalBean.proposal_name.equals(rowsBean.proposal_name)) {
                        proposalBean.delete();
                        ToastUtils.showShort(R.string.delete_success);
                        break;
                    }
                }
                if (null!=deteleListener){
                    deteleListener.deleteItem(rowsBean);
                }
            }
        });
    }

    public void setDeteleListener(DeteleListener deteleListener){
        this.deteleListener=deteleListener;
    }
}
