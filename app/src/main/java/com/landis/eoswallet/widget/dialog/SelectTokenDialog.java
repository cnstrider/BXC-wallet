package com.landis.eoswallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.adapter.BaseRecyclerAdapter;
import com.landis.eoswallet.databinding.ItemSelectTokenBinding;
import com.landis.eoswallet.net.model.AppTokenInfo;
import com.landis.eoswallet.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectTokenDialog extends Dialog {

    private BaseRecyclerAdapter<AppTokenInfo> adapter;

    public SelectTokenDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_select_token);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initView();
        loadTokenList();
    }

    private void initView() {
        List<AppTokenInfo> list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ((RecyclerView) findViewById(R.id.rv_select_token)).setLayoutManager(linearLayoutManager);

        adapter = new BaseRecyclerAdapter<AppTokenInfo>(list, R.layout.item_select_token){

            @Override
            protected void bindItem(BaseViewHolder holder, int position) {
                ItemSelectTokenBinding binding = (ItemSelectTokenBinding) holder.binding;
                binding.setData(getData(position));
                binding.getRoot().setOnClickListener(view -> {
                    if (null!=onItemClickListener){
                        onItemClickListener.onItemClick(getData(position).tokenname);
                        dismiss();
                    }
                });

            }
        };
        ((RecyclerView) findViewById(R.id.rv_select_token)).setAdapter(adapter);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
    }

    /**
     * 加载代币
     */
    private void loadTokenList() {

        String appTokenJson = GsonUtils.getInstance().readJsonFile(getContext(), "AppToken.json");

        List<AppTokenInfo> appTokenInfos = GsonUtils.getInstance().fromJson(appTokenJson, new TypeToken<List<AppTokenInfo>>() {
        }.getType());
        if (appTokenInfos != null && appTokenInfos.size() > 0) {
            adapter.clean();
            adapter.update(appTokenInfos);
        }
    }


    //私有属性
    private OnItemClickListener onItemClickListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(String selectToken);
    }
}
