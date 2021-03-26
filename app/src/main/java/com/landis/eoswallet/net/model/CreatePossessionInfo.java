package com.landis.eoswallet.net.model;

import java.util.List;

public class CreatePossessionInfo {


    /**
     * action : updateauth
     * args : {"account":"o1dc5shqblr","auth":{"accounts":[{"permission":{"actor":"zvj55e2zmct","permission":"active"},"weight":1},{"permission":{"actor":"o1dc5shqblr","permission":"activee"},"weight":1}],"keys":[],"threshold":2,"waits":[]},"parent":"owner","permission":"active"}
     * code : eosio
     */

    private String action;
    private ArgsBean args;
    private String code;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArgsBean getArgs() {
        return args;
    }

    public void setArgs(ArgsBean args) {
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ArgsBean {
        /**
         * account : o1dc5shqblr
         * auth : {"accounts":[{"permission":{"actor":"zvj55e2zmct","permission":"active"},"weight":1},{"permission":{"actor":"o1dc5shqblr","permission":"activee"},"weight":1}],"keys":[],"threshold":2,"waits":[]}
         * parent : owner
         * permission : active
         */

        private String account;
        private AuthBean auth;
        private String parent;
        private String permission;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public AuthBean getAuth() {
            return auth;
        }

        public void setAuth(AuthBean auth) {
            this.auth = auth;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public static class AuthBean {
            /**
             * accounts : [{"permission":{"actor":"zvj55e2zmct","permission":"active"},"weight":1},{"permission":{"actor":"o1dc5shqblr","permission":"activee"},"weight":1}]
             * keys : []
             * threshold : 2
             * waits : []
             */

            private int threshold;
            private List<AccountsBean> accounts;
            private List<?> keys;
            private List<?> waits;

            public int getThreshold() {
                return threshold;
            }

            public void setThreshold(int threshold) {
                this.threshold = threshold;
            }

            public List<AccountsBean> getAccounts() {
                return accounts;
            }

            public void setAccounts(List<AccountsBean> accounts) {
                this.accounts = accounts;
            }

            public List<?> getKeys() {
                return keys;
            }

            public void setKeys(List<?> keys) {
                this.keys = keys;
            }

            public List<?> getWaits() {
                return waits;
            }

            public void setWaits(List<?> waits) {
                this.waits = waits;
            }

            public static class AccountsBean {
                /**
                 * permission : {"actor":"zvj55e2zmct","permission":"active"}
                 * weight : 1
                 */

                private PermissionBean permission;
                private int weight;

                public PermissionBean getPermission() {
                    return permission;
                }

                public void setPermission(PermissionBean permission) {
                    this.permission = permission;
                }

                public int getWeight() {
                    return weight;
                }

                public void setWeight(int weight) {
                    this.weight = weight;
                }

                public static class PermissionBean {
                    /**
                     * actor : zvj55e2zmct
                     * permission : active
                     */

                    private String actor;
                    private String permission;

                    public String getActor() {
                        return actor;
                    }

                    public void setActor(String actor) {
                        this.actor = actor;
                    }

                    public String getPermission() {
                        return permission;
                    }

                    public void setPermission(String permission) {
                        this.permission = permission;
                    }
                }
            }
        }
    }
}
