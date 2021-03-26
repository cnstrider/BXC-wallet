package com.landis.eoswallet.net.manage;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.base.constant.SpConst;
import com.landis.eoswallet.event.Event;
import com.landis.eoswallet.ui.wallet.viewmodel.WalletViewModel;
import com.landis.eoswallet.widget.dialog.PurseInputDialog;

import org.greenrobot.eventbus.EventBus;

public class EosVoteManger {

    private static EosVoteManger INSTANCE;

    private PurseInputDialog inputDialog;

    public static EosVoteManger getInstance() {
        if (INSTANCE == null) INSTANCE = new EosVoteManger();
        return INSTANCE;
    }

    /**
     * 投票 赎回  领取分红
     *
     * @param bpname
     * @param stake
     * @param actionName
     * @param voteType
     */
    public void vote(String bpname, String stake, String actionName, int voteType,Context context) {

        if (inputDialog == null) {
            inputDialog = new PurseInputDialog(context);
        }

        inputDialog.setData("", "", bpname, stake, view -> {
            String key = (String) view.getTag();
            String text = SPUtils.getInstance().getString(SpConst.P_K);
            WalletViewModel walletVM = new WalletViewModel();
            byte[] bytes = EncryptUtils.decryptHexStringAES(text,
                    EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(), "AES/CBC" +
                            "/PKCS5Padding", walletVM.hexString2Bytes(walletVM.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong(context.getResources().getString(R.string.password_error));
                return;
            }

            String keyStr = new String(bytes);
            EosTransferManger.getInstance().vote(keyStr, inputDialog.to, inputDialog.quantity,
                    actionName, (isSuccess, o) -> {

                        if (isSuccess) {
//                                String sinfo = (String) o;
//                                String[] infos = sinfo.split("time=");
                            String toastMessage = "";
                            switch (voteType) {
                                case 1:
                                    toastMessage =
                                            context.getResources().getString(R.string.vote_modified_successfully);
                                    break;
                                case 2:
                                    toastMessage =
                                            context.getResources().getString(R.string.vote_successfully);
                                    break;
                                case 3:
                                    toastMessage =
                                            context.getResources().getString(R.string.receive_bonus_successfully);
                                    break;
                                case 4:
                                    toastMessage =
                                            context.getResources().getString(R.string.unfreezing_succeeded);
                                    break;
                            }
                            voteSuccess(toastMessage,context);
                            inputDialog.dismiss();
                        } else {
                            String message = null;
                            switch (voteType) {
                                case 1:
                                    message =
                                            context.getResources().getString(R.string.redemption_vote_failed);
                                    break;
                                case 2:
                                    message = context.getResources().getString(R.string.the_vote_failed);
                                    break;
                                case 3:
                                    message =
                                            context.getResources().getString(R.string.failure_bonus);
                                    break;
                                case 4:
                                    message = context.getResources().getString(R.string.unfreezing_failed);
                                    break;
                            }
                            ToastUtils.showShort(message + o.toString());
                            LogUtils.d("投票失败===" + message + o.toString());
                        }
                    });
        });
        if (!inputDialog.isShowing())
            inputDialog.show();
    }

    /**
     * 操作成功 通知
     * @param toastContent
     */
    private void voteSuccess(String toastContent,Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_success, null);
        TextView tvToastMsg = (TextView) view.findViewById(R.id.tv_toast_content);
        tvToastMsg.setText(toastContent);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
        EventBus.getDefault().post(new Event.RefreshVote());
    }
}
