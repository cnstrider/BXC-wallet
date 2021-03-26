package com.landis.eoswallet.net.model;

import java.util.List;

/**
 * 第几届投票
 */
public class ProducerScheduleInfo {

    /**
     * active : {"version":3,"producers":[{"producer_name":"biosbpd","block_signing_key":"EOS6yAtYPsS9kovdfdjL4nmUMYFL5MENKdSBsXs3RMJNMLvqyGjYD"},{"producer_name":"biosbpe","block_signing_key":"EOS5qpFQRfJNdyz7Wrdteji7xjX9dPe6C3gawp5iFs9DvA2dNyhMY"},{"producer_name":"biosbpf","block_signing_key":"EOS4w7XLjSHUu8GzJsQvv7WjVKoktqSCCXh16vEeWDpjMsGtoVdkZ"},{"producer_name":"biosbpg","block_signing_key":"EOS5SqwstRDdfZ6u4CNdxd467YcyUznmJnAeeLxjSBGDJVz21n8R5"},{"producer_name":"biosbph","block_signing_key":"EOS6QWBUCHaKuGg6zjbHwGkyr6bmPcLVBypmftpwbFuwmBQBt4ZN5"},{"producer_name":"biosbpi","block_signing_key":"EOS6TuGcqfbdbdBFRuMoVFjpEpeh1VUBXkKLcZEKz1BA9M1FQAob2"},{"producer_name":"biosbpj","block_signing_key":"EOS8koDEz7STK3KYCnpR3MAJxYkRsrYKzsG34c7R78tMZzxNmy2eG"},{"producer_name":"biosbpk","block_signing_key":"EOS8EPgQPUxMmE5H1hbXiMvS68MEtEG74rn8rL5VtQm4i1iYCi8SL"},{"producer_name":"biosbpl","block_signing_key":"EOS5a4G3GjV24VWu4uif99Z9ysYkhhSuaDHNYmnKjrCNEWBddJLuo"},{"producer_name":"biosbpm","block_signing_key":"EOS8T4npW5eRE8kaBeLkVJsYA791WZAwmuNcMsEJs4abLExjES7vz"},{"producer_name":"biosbpn","block_signing_key":"EOS6ZNx7hkbtB2UZiWFJFLHRPAgAFjSLucLLJ7oTgTu4y1msFvqmL"},{"producer_name":"biosbpo","block_signing_key":"EOS8jDusnXnCdpR3rrtiMaG3zSXHDEYuu9n38rxsWgVhak93NeSYS"},{"producer_name":"biosbpp","block_signing_key":"EOS7LVsr6ED7C1EWaYjUJTthU788q6qqcD1oVR7suPY5PnaizuZPo"},{"producer_name":"biosbpq","block_signing_key":"EOS83Vm6P3ve8ni2k1gz5X9Wj2AwT4me7qyEaykTMcapD3ouJXmah"},{"producer_name":"biosbpr","block_signing_key":"EOS6hP4kDrhXT9xtePn8wUvYaj25FeDFD4Qmmp92sca6az6ujg6pR"},{"producer_name":"biosbps","block_signing_key":"EOS6VCwo9ZZov1kuQp4Sv3dQTuV3sdghcDRDmV5KHtQ2byDN2DqD1"},{"producer_name":"biosbpt","block_signing_key":"EOS6vFz64vGiebUotyLhn1EB85hHnL7Xv9AsiNbC1j4y2ohmD1B2q"},{"producer_name":"biosbpu","block_signing_key":"EOS8NNgzeFMfHeikPf57vfcLZ6u6H5xqqz2uqC2GqZ4MzR3uzJawJ"},{"producer_name":"biosbpv","block_signing_key":"EOS6u2Fqv76spWGvx9DpGkRwbbZs46BHwidHiE89oBNVkYp8JbYFR"},{"producer_name":"biosbpw","block_signing_key":"EOS5D1n7ETER8LeV4saanZNRG29MsVHuSXjpyDtotDR1ciEfUFV74"},{"producer_name":"can1","block_signing_key":"EOS5TKoJXh2dLqyg1cqkhNjUskvygZ8p8JMiZCHg9kRazCiYi4umy"},{"producer_name":"can2","block_signing_key":"EOS7rqRQf2xZJozwwQUccS8z6hZYmwQwXHZdRLaYsd4DowtjcHZLy"},{"producer_name":"xiaosheng","block_signing_key":"EOS4yjhYcEbxuNtq2iqVtkNnhDidQzwBPU216d9GoFwFUL4bst2rZ"}]}
     * pending : null
     * proposed : null
     */

    public ActiveBean active;
    public Object pending;
    public Object proposed;



    public static class ActiveBean {
        /**
         * version : 3
         * producers : [{"producer_name":"biosbpd","block_signing_key":"EOS6yAtYPsS9kovdfdjL4nmUMYFL5MENKdSBsXs3RMJNMLvqyGjYD"},{"producer_name":"biosbpe","block_signing_key":"EOS5qpFQRfJNdyz7Wrdteji7xjX9dPe6C3gawp5iFs9DvA2dNyhMY"},{"producer_name":"biosbpf","block_signing_key":"EOS4w7XLjSHUu8GzJsQvv7WjVKoktqSCCXh16vEeWDpjMsGtoVdkZ"},{"producer_name":"biosbpg","block_signing_key":"EOS5SqwstRDdfZ6u4CNdxd467YcyUznmJnAeeLxjSBGDJVz21n8R5"},{"producer_name":"biosbph","block_signing_key":"EOS6QWBUCHaKuGg6zjbHwGkyr6bmPcLVBypmftpwbFuwmBQBt4ZN5"},{"producer_name":"biosbpi","block_signing_key":"EOS6TuGcqfbdbdBFRuMoVFjpEpeh1VUBXkKLcZEKz1BA9M1FQAob2"},{"producer_name":"biosbpj","block_signing_key":"EOS8koDEz7STK3KYCnpR3MAJxYkRsrYKzsG34c7R78tMZzxNmy2eG"},{"producer_name":"biosbpk","block_signing_key":"EOS8EPgQPUxMmE5H1hbXiMvS68MEtEG74rn8rL5VtQm4i1iYCi8SL"},{"producer_name":"biosbpl","block_signing_key":"EOS5a4G3GjV24VWu4uif99Z9ysYkhhSuaDHNYmnKjrCNEWBddJLuo"},{"producer_name":"biosbpm","block_signing_key":"EOS8T4npW5eRE8kaBeLkVJsYA791WZAwmuNcMsEJs4abLExjES7vz"},{"producer_name":"biosbpn","block_signing_key":"EOS6ZNx7hkbtB2UZiWFJFLHRPAgAFjSLucLLJ7oTgTu4y1msFvqmL"},{"producer_name":"biosbpo","block_signing_key":"EOS8jDusnXnCdpR3rrtiMaG3zSXHDEYuu9n38rxsWgVhak93NeSYS"},{"producer_name":"biosbpp","block_signing_key":"EOS7LVsr6ED7C1EWaYjUJTthU788q6qqcD1oVR7suPY5PnaizuZPo"},{"producer_name":"biosbpq","block_signing_key":"EOS83Vm6P3ve8ni2k1gz5X9Wj2AwT4me7qyEaykTMcapD3ouJXmah"},{"producer_name":"biosbpr","block_signing_key":"EOS6hP4kDrhXT9xtePn8wUvYaj25FeDFD4Qmmp92sca6az6ujg6pR"},{"producer_name":"biosbps","block_signing_key":"EOS6VCwo9ZZov1kuQp4Sv3dQTuV3sdghcDRDmV5KHtQ2byDN2DqD1"},{"producer_name":"biosbpt","block_signing_key":"EOS6vFz64vGiebUotyLhn1EB85hHnL7Xv9AsiNbC1j4y2ohmD1B2q"},{"producer_name":"biosbpu","block_signing_key":"EOS8NNgzeFMfHeikPf57vfcLZ6u6H5xqqz2uqC2GqZ4MzR3uzJawJ"},{"producer_name":"biosbpv","block_signing_key":"EOS6u2Fqv76spWGvx9DpGkRwbbZs46BHwidHiE89oBNVkYp8JbYFR"},{"producer_name":"biosbpw","block_signing_key":"EOS5D1n7ETER8LeV4saanZNRG29MsVHuSXjpyDtotDR1ciEfUFV74"},{"producer_name":"can1","block_signing_key":"EOS5TKoJXh2dLqyg1cqkhNjUskvygZ8p8JMiZCHg9kRazCiYi4umy"},{"producer_name":"can2","block_signing_key":"EOS7rqRQf2xZJozwwQUccS8z6hZYmwQwXHZdRLaYsd4DowtjcHZLy"},{"producer_name":"xiaosheng","block_signing_key":"EOS4yjhYcEbxuNtq2iqVtkNnhDidQzwBPU216d9GoFwFUL4bst2rZ"}]
         */

        public int version;
        public List<ProducersBean> producers;


        public static class ProducersBean {
            /**
             * producer_name : biosbpd
             * block_signing_key : EOS6yAtYPsS9kovdfdjL4nmUMYFL5MENKdSBsXs3RMJNMLvqyGjYD
             */

            public String producer_name;
            public String block_signing_key;

        }
    }
}
