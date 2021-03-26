package com.landis.eoswallet.ui.home.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.landis.eoswallet.R;
import com.landis.eoswallet.base.fragment.BaseFragment;
import com.landis.eoswallet.databinding.FragmentAppBinding;
import com.landis.eoswallet.ui.home.adapter.AppInfoAdapter;
import com.landis.eoswallet.ui.home.viewmodel.AppViewModel;

public class AppFragment extends BaseFragment<FragmentAppBinding, AppViewModel> {


    @Override
    protected void init() {
        mDataBinding.rvApp.setAdapter(new AppInfoAdapter(mViewModel.appInfoObservableList));
        mDataBinding.rvApp.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.loadAppData(getActivity());

        mDataBinding.searchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
        mDataBinding.searchView.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
        TextView searchTextView = mDataBinding.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchTextView.setTextSize(14);
        ImageView searchIcon = mDataBinding.searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(null);
        searchIcon.setVisibility(View.GONE);
        mDataBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.search(query,mDataBinding.searchView,searchTextView);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void bindViewModel() {
        mDataBinding.setViewModel(mViewModel);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_app;
    }

    @Override
    protected void initViewModel() {
        mViewModel = new AppViewModel();
    }
}
