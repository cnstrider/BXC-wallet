package com.landis.eoswallet.ui.other;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.activity.BaseActivity;
import com.landis.eoswallet.base.constant.RouteConst;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.base.viewmodel.BaseViewModel;
import com.landis.eoswallet.databinding.ActivityWebBinding;
import com.landis.eoswallet.net.manage.EosAccountManger;
import com.landis.eoswallet.net.manage.EosTransferManger;
import com.landis.eoswallet.net.model.JsTransactionInfo;
import com.landis.eoswallet.net.model.WalletInfo;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.util.LifecycleCallbacks;
import com.landis.eoswallet.util.WebUtils;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

@Route(path = RouteConst.AV_WEB)
public class WebActivity extends BaseActivity<ActivityWebBinding,BaseViewModel> {

    @Autowired(name = RouteConst.LINK)
    public String link;

    private PurseInputDialog purseInputDialog;

    @Override
    protected void init() {
        //设置窗口格式
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        if (TextUtils.isEmpty(link)){
            return;
        }

        mDataBinding.webView.loadUrl(link);

        mDataBinding.ivWebViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDataBinding.ivWebViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webViewSetting();

    }

    /**
     * webview 设置
     */
    @SuppressLint("JavascriptInterface")
    private void webViewSetting(){
        WebSettings webSettings = mDataBinding.webView.getSettings();
        //启用  JS
        webSettings.setJavaScriptEnabled(true);
        //自动调用js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //启用文件访问
        webSettings.setAllowFileAccess(true);
        //布局算法  会重新布局  默认NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //是否支持缩放
        webSettings.setSupportZoom(true);
        //是否使用内置缩放机制
        webSettings.setBuiltInZoomControls(true);
        //启用viewport 支持
        webSettings.setUseWideViewPort(true);
        //是否支持多窗口
        webSettings.setSupportMultipleWindows(true);
        //是否应启用应用程序缓存API
        webSettings.setAppCacheEnabled(true);
        //设置是否启用DOM存储API
        webSettings.setDomStorageEnabled(true);
        //设置是否启用地理位置
        webSettings.setGeolocationEnabled(true);
        //设置应用程序缓存内容的最大大小
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        //按需要启用或禁用插件
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setAppCachePath(getApplication().getDir("appcache", 0).getPath());
        webSettings.setDatabasePath(getApplication().getDir("databases", 0).getPath());

        mDataBinding.webView.addJavascriptInterface(this,"EosWallet");
        mDataBinding.webView.setWebViewClient(webViewClient);
        mDataBinding.webView.setWebChromeClient(webChromeClient);
    }

    @Override
    protected void bindViewModel() {

    }

    @Override
    protected void initViewModel() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web;
    }

    @Override
    public void onBackPressed() {
        if (null!=mDataBinding&&null!=mDataBinding.webView&&mDataBinding.webView.canGoBack()){
            mDataBinding.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 防止加载网页时调起系统浏览器
     */
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.d("链接地址"+url);

            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                return true;
            } else {
                if (LifecycleCallbacks.currentActivity() == null) {
                    return true;
                }
                return WebUtils.openApp(WebActivity.this, url, "您的手机未检测到相应的应用，请先下载");
            }
        }
    };

    /**
     * 控制进度条的显示与隐藏
     */
    private WebChromeClient webChromeClient = new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            LogUtils.d("网页加载进度"+newProgress);
            if (100==newProgress){
                mDataBinding.progressBar.setVisibility(View.GONE);
            }else{
                mDataBinding.progressBar.setProgress(newProgress);
            }

        }
    };

    @JavascriptInterface
    public void getInfo(String callback) {

        runOnUiThread(() -> {
            if ( null== mDataBinding) return;
            WalletInfo walletInfo = new WalletInfo();
            walletInfo.authToken = SPUtils.getInstance().getString(SpConst.UID);
            walletInfo.walletName = SPUtils.getInstance().getString(SpConst.WALLET_NAME);
            mDataBinding.webView.loadUrl("javascript:" + callback + "(" + new Gson().toJson(walletInfo) +
                    ")");
        });
    }

    @JavascriptInterface
    public void transaction(String to, String quantity, String remark, String callback) {
        runOnUiThread(() -> {
            if ( null== mDataBinding) return;
            if ( null== purseInputDialog) {
                purseInputDialog = new PurseInputDialog(this);
            }
            String action = "eosio";
            String currency = quantity.split(" ")[1];
            if (!TextUtils.equals(currency, "BXC") && !TextUtils.equals(currency, "EOS")) {
                action = "eosio.token";
            }

            purseInputDialog.setData(action, remark, to, quantity, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = (String) v.getTag();
                    String text = SPUtils.getInstance().getString(SpConst.P_K);
                    WalletViewModel walletVM = new WalletViewModel();
                    byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
                    if (bytes == null || bytes.length == 0) {
                        ToastUtils.showLong("密码错误");
                        return;
                    }
                    String pri = new String(bytes);
//                ToastUtils.showShort(pri);
                    EosTransferManger.getInstance().pay(pri, purseInputDialog.action, purseInputDialog.remark, purseInputDialog.to, purseInputDialog.quantity, SPUtils.getInstance().getString(SpConst.WALLET_NAME), (isSuccess, o) -> {
                        if (isSuccess) {
                            String sinfo = (String) o;
                            String[] infos = sinfo.split("time=");
                            ToastUtils.showLong("交易成功! transaction_id=" + infos[0]);
                            JsTransactionInfo info = new JsTransactionInfo();
                            info.msg = "交易成功";
                            info.tid = infos[0];

                            purseInputDialog.dismiss();
                            mDataBinding.webView.loadUrl("javascript:" + callback + "(" + new Gson().toJson(info) + ")");
                        } else {
                            ToastUtils.showLong("交易失败" + o.toString());
                        }
                    });
                }
            });

            if (!purseInputDialog.isShowing())
                purseInputDialog.show();
        });
    }

    /**
     * * 投票
     *
     * @param bpname   超级节点 账户名
     * @param stake    投票金额
     * @param callback js 回调
     */
    @JavascriptInterface
    public void vote(String bpname, String stake, String actionName, String callback) {

        runOnUiThread(() -> {
            if ( null== mDataBinding) return;
            if ( null==  purseInputDialog) {
                purseInputDialog = new PurseInputDialog(this);
            }

            String currency = stake.split(" ")[1];
            purseInputDialog.setData("", "", bpname, stake, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = (String) view.getTag();
                    String text = SPUtils.getInstance().getString(SpConst.P_K);
                    WalletViewModel walletVM = new WalletViewModel();
                    byte[] bytes = EncryptUtils.decryptHexStringAES(text, EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
                    if (bytes == null || bytes.length == 0) {
                        ToastUtils.showLong("密码错误");
                        return;
                    }

                    String keyStr = new String(bytes);
                    EosTransferManger.getInstance().vote(keyStr, purseInputDialog.to, purseInputDialog.quantity, actionName, new EosTransferManger.Callback() {
                        @Override
                        public void onback(boolean isSuccess, Object o) {
                            if (isSuccess) {
                                String sinfo = (String) o;
                                String[] infos = sinfo.split("time=");
//                                ToastUtils.showLong("投票成功! transaction_id=" + infos[0]);

                                String balance = EosAccountManger.getInstance().getBalance(currency);
//                            H5AppAty.this.serverCallback(SPUtils.getInstance().getString(SpConst.WALLET_NAME), balance, currency, SPUtils.getInstance().getString(SpConst.WALLET_NAME), to, quantity.split(" ")[0], (String) o, remark);
                                JsTransactionInfo info = new JsTransactionInfo();
                                if (actionName.equals("vote")) {
                                    info.msg = "投票成功"+"投票金额："+stake+"节点名称："+bpname;
                                } else if (actionName.equals("unfreeze")) {
                                    info.msg = "赎回投票成功";
                                } else if (actionName.equals("claim")) {
                                    info.msg = "分红成功";
                                }
                                info.tid = infos[0];

                                purseInputDialog.dismiss();
                                mDataBinding.webView.loadUrl("javascript:" + callback + "(" + new Gson().toJson(info) + ")");
                            } else {
                                String message = null;
                                if (actionName.equals("vote")) {
                                    message = "投票失败";
                                } else if (actionName.equals("unfreeze")) {
                                    message = "赎回投票失败";
                                } else if (actionName.equals("claim")) {
                                    message = "分红失败";
                                }
                                ToastUtils.showLong(message + o.toString());
                            }
                        }
                    });
                }
            });
            if (!purseInputDialog.isShowing())
                purseInputDialog.show();
        });
    }
}
