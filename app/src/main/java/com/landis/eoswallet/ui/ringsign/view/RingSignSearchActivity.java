package com.landis.eoswallet.ui.ringsign.view;

import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.databinding.ActivityRingSignSearchBinding;
import com.landis.eoswallet.ui.ringsign.viewmodel.RingSignSearchViewModel;

/**
 * 搜索环签账号界面
 */
@Route(path = RouteConst.AV_RING_SIGN_SEARCH)
public class RingSignSearchActivity extends BaseActivity<ActivityRingSignSearchBinding,
        RingSignSearchViewModel> {

    @Autowired(name = "search_key")
    public String searchKey;

    @Override
    protected void init() {
        setAppBarView(mDataBinding.appbar);
        mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
        mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
        TextView searchTextView =
                mDataBinding.searchViewRingSign.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchTextView.setTextSize(13);
        searchTextView.setTextColor(Color.GRAY);
        searchTextView.setHintTextColor(Color.GRAY);
        searchTextView.setGravity(Gravity.CENTER);
        searchTextView.setMaxHeight(30);
        if (!StringUtils.isEmpty(searchKey)) {
            searchTextView.setText(searchKey);
        }

        if (!StringUtils.isEmpty(searchKey)) {
            mViewModel.searchRingSignAccount(searchKey);
        }

        mDataBinding.searchViewRingSign.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mDataBinding.searchViewRingSign.clearFocus();
                searchKey=s;
                mViewModel.searchRingSignAccount(searchKey);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mDataBinding.ivRingSignBack.setOnClickListener(v -> finish());

        mDataBinding.tvRingSignSearchCancel.setOnClickListener(v -> finish());

        mDataBinding.tvRingSignAccountFollow.setOnClickListener(v -> mViewModel.followRingSignAccount());
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
        mViewModel = new RingSignSearchViewModel();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ring_sign_search;
    }
}
