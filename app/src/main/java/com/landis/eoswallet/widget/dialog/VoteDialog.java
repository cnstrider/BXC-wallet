package com.landis.eoswallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.landis.eoswallet.R;
import com.landis.eoswallet.net.model.VoteNodesList;

public class VoteDialog extends Dialog {

    //投票节点信息
    private VoteNodesList.VoteNodes voteNodes;
    //余额
    private float balance = 0;
    private int voteType;
    //投票类型
    private TextView tvVoteType;
    //超级节点名称
    private TextView tvvoteSuperNode;
    //当前投票金额
    private TextView tvVoteStaked;
    //可用投票金额
    private TextView tvVoteAvailableStaked;
    //修改后投票金额
    private TextView tvChangeVoteNumber;
    //输入投票金额
    private EditText edVoteNumber;
    //关闭
    private ImageView ivCloseVote;
    //投票
    private TextView tvVoteContinue;

    //赎回
    private boolean redeemVote = false;

    private RadioGroup radioGroupVote;

    public VoteDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_vote);
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        initView();
    }

    private void initView() {
        tvVoteType = findViewById(R.id.tv_vote_type);
        tvvoteSuperNode = findViewById(R.id.tv_vote_super_node);
        tvVoteStaked = findViewById(R.id.tv_vote_staked);
        tvVoteAvailableStaked = findViewById(R.id.tv_vote_available_staked);
        tvChangeVoteNumber = findViewById(R.id.tv_change_vote_number);
        edVoteNumber = findViewById(R.id.ed_vote_number);
        ivCloseVote = findViewById(R.id.iv_close_vote);
        tvVoteContinue = findViewById(R.id.tv_vote_continue);
        radioGroupVote = findViewById(R.id.radio_group_vote);
        ivCloseVote.setOnClickListener(view -> dismiss());

        edVoteNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (redeemVote) {
                    tvChangeVoteNumber.setText("0 BXC");
                } else {
                    tvChangeVoteNumber.setText(editable + " BXC");
                }
            }
        });

        tvVoteContinue.setOnClickListener(view -> {
            if (StringUtils.isEmpty(edVoteNumber.getText().toString())) {
                ToastUtils.showShort(getContext().getResources().getString(R.string.enter_vote_number));
                return;
            }
            if (2==voteType&&0==Integer.parseInt(edVoteNumber.getText().toString().trim())){
                ToastUtils.showShort(getContext().getResources().getString(R.string.vote_number_zero));
                return;
            }

            if ((redeemVote && (balance - 0.05 > 0))
                    || (!redeemVote && (balance - (Integer.parseInt(edVoteNumber.getText().toString()) + 0.05
                    - (StringUtils.isEmpty(voteNodes.vote_staked) ? 0 : Float.valueOf(voteNodes.vote_staked.replace("EOS", "").trim()))) > 0))) {
                if (null != onItemClickListener) {
                    if (redeemVote) {
                        onItemClickListener.onItemClick(0, voteNodes, voteType);
                    } else {
                        onItemClickListener.onItemClick(Integer.parseInt(edVoteNumber.getText().toString()), voteNodes, voteType);

                    }
                    dismiss();
                }
            } else {
                ToastUtils.showShort(getContext().getResources().getString(R.string.balance_insufficient));
            }
        });

        radioGroupVote.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_new_vote:
                    edVoteNumber.getText().clear();
                    edVoteNumber.setFocusableInTouchMode(true);
                    edVoteNumber.setFocusable(true);
                    redeemVote = false;
                    break;
                case R.id.radio_redeem_vote:
                    redeemVote = true;
                    edVoteNumber.setText(voteNodes.vote_staked.replace("EOS", "").trim());
                    edVoteNumber.setFocusable(false);
                    edVoteNumber.setFocusableInTouchMode(false);
                    break;
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
     *
     * @param voteNodes
     * @param balance
     * @param voteType  1：修改 2：投票
     */
    public void setData(VoteNodesList.VoteNodes voteNodes, Float balance, int voteType) {
        this.balance = balance;
        this.voteNodes = voteNodes;
        this.voteType = voteType;
        tvVoteType.setText(1 == voteType ? getContext().getResources().getString(R.string.modify_node_vote) : getContext().getResources().getString(R.string.node_vote));
        radioGroupVote.setVisibility(1 == voteType ? View.VISIBLE : View.GONE);
        tvvoteSuperNode.setText(voteNodes.name);
        tvVoteStaked.setText((StringUtils.isEmpty(voteNodes.vote_staked) ? "0 BXC" : voteNodes.vote_staked+" BXC"));
        tvVoteAvailableStaked.setText(balance + " BXC");
    }

    //私有属性
    private OnItemClickListener onItemClickListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(int voteNumber, VoteNodesList.VoteNodes voteNodes, int voteType);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        edVoteNumber.getText().clear();
        edVoteNumber.setFocusableInTouchMode(true);
        edVoteNumber.setFocusable(true);
        redeemVote = false;
        radioGroupVote.check(R.id.radio_new_vote);
    }
}
