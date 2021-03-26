package com.landis.eoswallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.net.model.VoteNodesList;

/**
 * 分红
 */
public class ReceiveClaimDialog extends Dialog {

    private TextView tvReceiveClaimSuperNode;
    private TextView tvReceiveClaimNumber;
    private TextView tvReceiveClaimFree;
    private TextView tvReceiveClaimContinue;
    private ImageView ivCloseReceiveClaim;

    private TextView tvTitleReceive;
    private TextView tvReceiveTypeNumber;
    //投票节点信息
    private VoteNodesList.VoteNodes voteNodes;
    //4：解冻 3：分红
    private   int voteType;

    public ReceiveClaimDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_receive_claim);
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        initView();
    }

    private void initView() {
        tvTitleReceive= findViewById(R.id.tv_title_receive);
        tvReceiveTypeNumber= findViewById(R.id.tv_receive_type_number);
        tvReceiveClaimSuperNode = findViewById(R.id.tv_receive_claim_super_node);
        tvReceiveClaimNumber = findViewById(R.id.tv_receive_claim_number);
        tvReceiveClaimFree = findViewById(R.id.tv_receive_claim_free);
        tvReceiveClaimContinue = findViewById(R.id.tv_receive_claim_continue);
        ivCloseReceiveClaim = findViewById(R.id.iv_close_receive_claim);

        ivCloseReceiveClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvReceiveClaimContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null!=onItemReceiveClaimClickListener){
                    onItemReceiveClaimClickListener.onItemClick(voteNodes,voteType);
                    dismiss();
                }
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
    }

    /**
     * 设置数据
     * @param voteNodes
     * @param voteType 3:分红
     */
    public void setData(VoteNodesList.VoteNodes voteNodes,  int voteType) {
        this.voteNodes = voteNodes;
        this.voteType = voteType;
        tvTitleReceive.setText(4==voteType?getContext().getResources().getString(R.string.unblocked_assets):getContext().getResources().getString(R.string.receive_bonus));
        tvReceiveTypeNumber.setText(4==voteType?getContext().getResources().getString(R.string.unfreezing_amount):getContext().getResources().getString(R.string.extractable_balance));
        tvReceiveClaimFree.setText(4==voteType?"0.01 BXC":"0.03 BXC");
        tvReceiveClaimSuperNode.setText(voteNodes.name);
        if (4==voteType){
            tvReceiveClaimNumber.setText((StringUtils.isEmpty(voteNodes.unstaking) ?"0":voteNodes.unstaking.replace("EOS","BXC").trim()) + " BXC");
        }else{
            tvReceiveClaimNumber.setText((StringUtils.isEmpty(voteNodes.claim_balance) ?"0":voteNodes.claim_balance.replace("EOS","BXC").trim()) + " BXC");
        }
    }

    //私有属性
    private OnItemReceiveClaimClickListener onItemReceiveClaimClickListener = null;

    //setter方法
    public void setOnItemReceiveClaimClickListener(OnItemReceiveClaimClickListener onItemReceiveClaimClickListener) {
        this.onItemReceiveClaimClickListener = onItemReceiveClaimClickListener;
    }

    //回调接口
    public interface OnItemReceiveClaimClickListener {
        void onItemClick(VoteNodesList.VoteNodes voteNodes, int voteType);
    }
}
