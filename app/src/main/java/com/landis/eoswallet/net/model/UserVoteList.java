package com.landis.eoswallet.net.model;

import java.util.List;

public class UserVoteList {

    public List<UserVote> rows;
    public boolean more;

    public class UserVote {

        /**
         * bpname : can1
         * staked : 20.0000 EOS
         * voteage : 24859
         * voteage_update_height : 769785
         * unstaking : 5.0000 EOS
         * unstake_height : 746813
         */

        public String bpname;
        public String staked;
        public long voteage;
        public long voteage_update_height;
        public String unstaking;
        public long unstake_height;

    }

}
