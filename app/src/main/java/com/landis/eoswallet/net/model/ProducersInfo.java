package com.landis.eoswallet.net.model;

import java.util.List;

/**
 * @desc: 投票节点信息
 * @projectName:LOS
 * @author:xuwh
 * @date:2020/8/23 0023 14:01
 * @UpdateUser： 更新者
 * @UpdateDate: 2020/8/23 0023 14:01
 * @UpdateRemark: 更新说明
 * @version:
 */
public class ProducersInfo {


    /**
     * rows : [{"version":10,"block_height":1031364,"producers":[{"bpname":"biosbpa","amount":43},{"bpname":"biosbpb","amount":43},{"bpname":"biosbph","amount":42},{"bpname":"biosbpi","amount":42},{"bpname":"biosbpj","amount":42},{"bpname":"biosbpk","amount":42},{"bpname":"biosbpm","amount":42},{"bpname":"biosbpn","amount":42},{"bpname":"biosbpo","amount":42},{"bpname":"biosbpp","amount":42},{"bpname":"biosbpq","amount":42},{"bpname":"biosbpr","amount":42},{"bpname":"biosbps","amount":42},{"bpname":"biosbpt","amount":42},{"bpname":"biosbpu","amount":42},{"bpname":"biosbpv","amount":42},{"bpname":"biosbpw","amount":42},{"bpname":"can1","amount":42},{"bpname":"can2","amount":43},{"bpname":"ctu1","amount":43},{"bpname":"ctu2","amount":43},{"bpname":"pek1","amount":43},{"bpname":"xiaosheng","amount":43}]}]
     * more : true
     */

    private boolean more;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * version : 10
         * block_height : 1031364
         * producers : [{"bpname":"biosbpa","amount":43},{"bpname":"biosbpb","amount":43},{"bpname":"biosbph","amount":42},{"bpname":"biosbpi","amount":42},{"bpname":"biosbpj","amount":42},{"bpname":"biosbpk","amount":42},{"bpname":"biosbpm","amount":42},{"bpname":"biosbpn","amount":42},{"bpname":"biosbpo","amount":42},{"bpname":"biosbpp","amount":42},{"bpname":"biosbpq","amount":42},{"bpname":"biosbpr","amount":42},{"bpname":"biosbps","amount":42},{"bpname":"biosbpt","amount":42},{"bpname":"biosbpu","amount":42},{"bpname":"biosbpv","amount":42},{"bpname":"biosbpw","amount":42},{"bpname":"can1","amount":42},{"bpname":"can2","amount":43},{"bpname":"ctu1","amount":43},{"bpname":"ctu2","amount":43},{"bpname":"pek1","amount":43},{"bpname":"xiaosheng","amount":43}]
         */

        public int version;
        public int block_height;
        public List<ProducersBean> producers;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getBlock_height() {
            return block_height;
        }

        public void setBlock_height(int block_height) {
            this.block_height = block_height;
        }

        public List<ProducersBean> getProducers() {
            return producers;
        }

        public void setProducers(List<ProducersBean> producers) {
            this.producers = producers;
        }

        public static class ProducersBean {
            /**
             * bpname : biosbpa
             * amount : 43
             */

            public String bpname;
            public int amount;

            public String getBpname() {
                return bpname;
            }

            public void setBpname(String bpname) {
                this.bpname = bpname;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }
        }
    }
}
