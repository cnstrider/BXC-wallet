package com.landis.eoswallet.net.manage;



import android.support.annotation.Nullable;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @desc: 创建账户json_to_bin
 * @author:xuwh
 * @date:2020/7/30 0030 22:19
 * @UpdateUser： 更新者
 * @UpdateDate: 2020/7/30 0030 22:19
 * @UpdateRemark: 更新说明
 * @version:
 */
public abstract class CallFactoryProxy  implements Call.Factory{
    private static final String NAME_BASE_URL = "BaseUrlName";
    private final Call.Factory delegate;

    public CallFactoryProxy(Call.Factory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call newCall(Request request) {
        String baseUrlName = request.header(NAME_BASE_URL);
        if (baseUrlName != null) {
            HttpUrl newHttpUrl = getNewUrl(baseUrlName, request);
            if (newHttpUrl != null) {
                Request newRequest = request.newBuilder().url(newHttpUrl).build();
                return delegate.newCall(newRequest);
            } else {
            }
        }
        return delegate.newCall(request);
    }

    @Nullable
    protected abstract HttpUrl getNewUrl(String baseUrlName, Request request);
}
