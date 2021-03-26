package com.landis.eoswallet.net.model;

import java.util.List;

public class ApprovalInfo {


    /**
     * rows : [{"proposal_name":"sfasfa","requested_approvals":[{"actor":"a13s3i55vvs","permission":"active"},{"actor":"j2nck4rv1gw","permission":"active"}],"provided_approvals":[]}]
     * more : false
     */

    public boolean more;
    public List<RowsBean> rows;
    public static class RowsBean {
        /**
         * proposal_name : sfasfa
         * requested_approvals : [{"actor":"a13s3i55vvs","permission":"active"},{"actor":"j2nck4rv1gw","permission":"active"}]
         * provided_approvals : []
         */

        public String proposal_name;
        public List<RequestedApprovalsBean> requested_approvals;
        public List<RequestedApprovalsBean> provided_approvals;

        public static class RequestedApprovalsBean {
            /**
             * actor : a13s3i55vvs
             * permission : active
             */

            public String actor;
            public String permission;
        }
    }
}
