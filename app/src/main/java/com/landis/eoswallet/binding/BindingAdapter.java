package com.landis.eoswallet.binding;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.landis.eoswallet.R;
import com.landis.eoswallet.enums.RefreshState;

public final class BindingAdapter {

    private BindingAdapter() {
    }

    /**
     * 设置RefreshLayout的加载更多
     *
     * @param refreshLayout RefreshLayout
     * @param hasMore       true表示还有更多，false表示没有更多了
     */
    @android.databinding.BindingAdapter("hasMore")
    public static void setHasMore(MaterialRefreshLayout refreshLayout, Boolean hasMore) {
        if (hasMore != null) {
            refreshLayout.setLoadMore(hasMore);
        }
    }

    /**
     * 设置RefreshLayout的刷新状态
     *
     * @param refreshLayout RefreshLayout
     * @param refreshState  刷新状态
     */
    @android.databinding.BindingAdapter("refreshState")
    public static void setRefreshState(MaterialRefreshLayout refreshLayout, RefreshState refreshState) {
        if (refreshState == null) {
            return;
        }

        switch (refreshState) {
            case REFRESH_END:
                refreshLayout.finishRefresh();
                break;

            case LOAD_MORE_END:
                refreshLayout.finishRefreshLoadMore();
                scrollToNextPosition(refreshLayout);
                break;

            default:
                break;
        }
    }

    /**
     * 设置ImageView的图片URL
     *
     * @param imageView ImageView
     * @param imageUrl  图片URL
     */
    @android.databinding.BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher)
                .into(imageView);
    }

    private static void scrollToNextPosition(MaterialRefreshLayout refreshLayout) {
        View child = refreshLayout.getChildAt(0);
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
            }
        }
    }
}

