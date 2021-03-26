package com.landis.eoswallet.net.model;

import java.util.List;

public class VoteNodesList {
    public List<VoteNodes> rows;
    public boolean more;
    public class VoteNodes {
        /**
         * name : can1
         * block_signing_key : EOS5TKoJXh2dLqyg1cqkhNjUskvygZ8p8JMiZCHg9kRazCiYi4umy
         * commission_rate : 5000
         * total_staked : 226
         * rewards_pool : 155080.5138 EOS
         * total_voteage : 131129613
         * voteage_update_height : 769785
         * url :
         * emergency : 0
         */

        public String name;
        public String block_signing_key;
        public long commission_rate;
        public long total_staked;
        public String rewards_pool;
        public long total_voteage;
        public long voteage_update_height;
        public String url;
        public int emergency;
        public int ranking;
        public boolean voted;
        //投票金额
        public String vote_staked;
        //分红
        public String claim_balance;
        //赎回中金额
        public String unstaking;
        //出块
        public String amount;
        //赎回高度
        public long unstake_height;
    }

}
