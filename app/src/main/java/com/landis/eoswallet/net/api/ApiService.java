package com.landis.eoswallet.net.api;

import com.google.gson.JsonObject;
import com.landis.eoswallet.base.constant.Constant;
import com.landis.eoswallet.net.model.ActionInfo;
import com.landis.eoswallet.net.model.ApprovalInfo;
import com.landis.eoswallet.net.model.BaseUserInfo;
import com.landis.eoswallet.net.model.ChainBlock;
import com.landis.eoswallet.net.model.ChainInfo;
import com.landis.eoswallet.net.model.EosAccountInfo;
import com.landis.eoswallet.net.model.ProducerScheduleInfo;
import com.landis.eoswallet.net.model.ProducersInfo;
import com.landis.eoswallet.net.model.ProposalInfo;
import com.landis.eoswallet.net.model.RequireFee;
import com.landis.eoswallet.net.model.UserAvailableList;
import com.landis.eoswallet.net.model.UserTokenAvailableList;
import com.landis.eoswallet.net.model.UserVoteList;
import com.landis.eoswallet.net.model.VoteNodesList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {


    @FormUrlEncoded
    @POST(Constant.get_key_accounts)
    Observable<BaseUserInfo> getKeyAccounts(@Field("public_key") String publicKey);

    //    @FormUrlEncoded
    @POST(Constant.get_abi_json_to_bin)
    Observable<JsonObject> json2bean(@Body JsonObject body);

    @Headers("BaseUrlName:json_bin")
    @POST(Constant.get_abi_json_to_bin)
    Observable<JsonObject> jsonTobean(@Body JsonObject body);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<UserAvailableList> getTableRows(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("table_key") String table_key, @Field("limit") int limit, @Field("json") boolean json);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<UserTokenAvailableList> getTokenBalance(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("key") String key, @Field("limit") int limit, @Field("json") boolean json);


    @GET(Constant.get_chain_info)
    Observable<ChainInfo> getChainInfo();


    @FormUrlEncoded
    @POST(Constant.get_chain_block)
    Observable<ChainBlock> getChainBlock(@Field("block_num_or_id") String id);

    @POST(Constant.get_required_fee)
    Observable<RequireFee> getRequiredFee(@Body JsonObject body);

    @POST(Constant.push_transaction)
    Observable<JsonObject> pushTransaction(@Body JsonObject body);

    @FormUrlEncoded
    @POST(Constant.get_account)
    Observable<JsonObject> checkAccount(@Field("account_name") String accountName);

    @FormUrlEncoded
    @POST(Constant.get_account)
    Observable<EosAccountInfo> checkJointlyAccount(@Field("account_name") String accountName);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<UserVoteList> getVoteRows(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("json") boolean json, @Field("limit") int limit);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<VoteNodesList> getMyVote(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("table_key") String table_key, @Field("json") boolean json);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<VoteNodesList> getVoteNodes(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("json") boolean json, @Field("limit") int limit);

    @GET(Constant.get_producer_schedule)
    Observable<ProducerScheduleInfo> getProducerSchedule();

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<ProducersInfo> getVoteBlockOut(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("table_key") String table_key, @Field("limit") int limit, @Field("json") boolean json);


    @FormUrlEncoded
    @POST(Constant.get_currency_stats)
    Observable<JsonObject> getCurrencyStats(@Field("code") String code,@Field("symbol") String symbol);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<ProposalInfo> getProposals(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("limit") int limit, @Field("json") boolean json);

    @FormUrlEncoded
    @POST(Constant.get_table_rows)
    Observable<ApprovalInfo> getApproves(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("limit") int limit, @Field("json") boolean json);

    @FormUrlEncoded
    @POST(Constant.get_actions)
    Observable<ActionInfo> getActions(@Field("pos") int pos, @Field("offset") int offset, @Field("account_name") String account_name);

    @POST("https://1peKQbFiDRmJc2SoEnKIC6ekrif:aa65bf041163822d9388184b183327dc@filecoin.infura.io")
    Observable<JsonObject> validateAddress(@Body JsonObject body);
}
