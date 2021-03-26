package com.landis.eoswallet.net.model;

import java.util.List;

public class EosAccountInfo {


    /**
     * account_name : kol.j3o1a.3
     * cpu_limit : {"available":-1,"max":-1,"used":-1}
     * cpu_weight : -1
     * created : 2020-10-20T01:55:21.000
     * head_block_num : 2593746
     * head_block_time : 2020-10-20T06:23:09.000
     * last_code_update : 1970-01-01T00:00:00.000
     * net_limit : {"available":-1,"max":-1,"used":-1}
     * net_weight : -1
     * permissions : [{"parent":"owner","perm_name":"active","required_auth":{"accounts":[{"permission":{"actor":"can1","permission":"active"},"weight":1},{"permission":{"actor":"kol.j3o1a.3","permission":"active"},"weight":1}],"keys":[],"threshold":2,"waits":[]}},{"parent":"","perm_name":"owner","required_auth":{"accounts":[],"keys":[{"key":"EOS6EfwqwaptQGcSMWUZ1ZtKxsFBrJXmaAXQy3SFvE3Nijyiw1Stp","weight":1}],"threshold":1,"waits":[]}}]
     * privileged : false
     * ram_quota : -1
     * ram_usage : 2738
     */

    public String account_name;
    public CpuLimitBean cpu_limit;
    public int cpu_weight;
    public String created;
    public int head_block_num;
    public String head_block_time;
    public String last_code_update;
    public NetLimitBean net_limit;
    public int net_weight;
    public boolean privileged;
    public int ram_quota;
    public int ram_usage;
    public List<PermissionsBean> permissions;


    public static class CpuLimitBean {
        /**
         * available : -1
         * max : -1
         * used : -1
         */

        public int available;
        public int max;
        public int used;

    }

    public static class NetLimitBean {
        /**
         * available : -1
         * max : -1
         * used : -1
         */

        public int available;
        public int max;
        public int used;

    }

    public static class PermissionsBean {
        /**
         * parent : owner
         * perm_name : active
         * required_auth : {"accounts":[{"permission":{"actor":"can1","permission":"active"},"weight":1},{"permission":{"actor":"kol.j3o1a.3","permission":"active"},"weight":1}],"keys":[],"threshold":2,"waits":[]}
         */

        public String parent;
        public String perm_name;
        public RequiredAuthBean required_auth;


        public static class RequiredAuthBean {
            /**
             * accounts : [{"permission":{"actor":"can1","permission":"active"},"weight":1},{"permission":{"actor":"kol.j3o1a.3","permission":"active"},"weight":1}]
             * keys : []
             * threshold : 2
             * waits : []
             */

            public int threshold;
            public List<AccountsBean> accounts;
            public List<KeysBean> keys;
            public List<?> waits;



            public static class AccountsBean {
                /**
                 * permission : {"actor":"can1","permission":"active"}
                 * weight : 1
                 */

                public PermissionBean permission;
                public int weight;


                public static class PermissionBean {
                    /**
                     * actor : can1
                     * permission : active
                     */
                    public String actor;
                    public String permission;
                }
            }

            public static class KeysBean {
                /**
                 * key : EOS4ufZoTw95yHJS6Cyz3h4w5a2W4cyYpMYRnd7gbFZuCfPxUFS6r
                 * weight : 1
                 */

                private String key;
                private int weight;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }
            }
        }
    }
}
