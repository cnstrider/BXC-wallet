package com.landis.eoswallet.net.model;

import java.util.List;

public class ActionInfo {


    public int last_irreversible_block;
    public List<ActionsBean> actions;

    public static class ActionsBean {
        public int global_action_seq;
        public int account_action_seq;
        public int block_num;
        public String block_time;
        public ActionTraceBean action_trace;

        public static class ActionTraceBean {
            public ReceiptBean receipt;
            public ActBean act;
            public boolean context_free;
            public int elapsed;
            public String console;
            public String trx_id;
            public int block_num;
            public String block_time;
            public String producer_block_id;
            public Object except;
            public List<?> account_ram_deltas;
            public List<?> inline_traces;

            public static class ReceiptBean {
                public String receiver;
                public String act_digest;
                public int global_sequence;
                public int recv_sequence;
                public int code_sequence;
                public int abi_sequence;
                public List<List<String>> auth_sequence;
            }

            public static class ActBean {
                public String account;
                public String name;
                public DataBean data;
                public String hex_data;
                public List<AuthorizationBean> authorization;

                public static class DataBean {
                    public String from;
                    public String to;
                    public String quantity;
                    public String memo;
                }

                public static class AuthorizationBean {
                    public String actor;
                    public String permission;
                }
            }
        }
    }
}
