package com.landis.eoswallet.net.model;

import android.text.TextUtils;

import com.landis.eoswallet.util.eos.Utils;
import com.landis.eoswallet.util.eos.digest.Sha256;
import com.landis.eoswallet.util.eos.types.EosByteWriter;
import com.landis.eoswallet.util.eos.types.EosType;
import com.landis.eoswallet.util.eos.types.TypeAsset;
import com.landis.eoswallet.util.eos.types.TypeChainId;
import com.landis.eoswallet.util.eos.types.TypeName;
import com.landis.eoswallet.util.eos.util.HexUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TransactionRequest implements EosType.Packer {
    public List<String> ctxFreeData;
    public List<String> signatures;
    public List<Action> actions;
    public List<Action> context_free_actions;
    public List<String> transaction_extensions;

    public String fee;

    public long delay_sec;
    public String expiration;
    public long max_cpu_usage_ms;
    public long max_net_usage_words;
    public long ref_block_num;
    public long ref_block_prefix;


    public static class Action implements EosType.Packer {
        public String account;
        public List<Auth> authorization;
        public String data;
        public String name;

        @Override
        public void pack(EosType.Writer writer) {
            if(account != null) {
                new TypeName(account).pack(writer);
            }
            if(!TextUtils.isEmpty(name)) {
                new TypeName(name).pack(writer);
            }

            writer.putCollection(authorization);

            if (null != data) {//"000000000090b1ca00cc2ec313b256c08813000000000000044c4150000000000652686667676a"
                byte[] dataAsBytes = HexUtils.toBytes(data);
                writer.putVariableUInt((long)dataAsBytes.length);//size 39
                writer.putBytes(dataAsBytes);//81
            } else {
                writer.putVariableUInt(0);
            }
        }
    }

    public static class Auth implements EosType.Packer {
        public String actor;
        public String permission;

        @Override
        public void pack(EosType.Writer writer) {
            new TypeName(actor).pack(writer);
            new TypeName(permission).pack(writer);
        }
    }

    @Override
    public void pack(EosType.Writer writer) {
        writer.putIntLE((int)(getExpirationAsDate(expiration).getTime() / 1000)); // ms -> sec
        writer.putShortLE((short) (ref_block_num & 0xFFFF));  // uint16
        writer.putIntLE((int)(ref_block_prefix & -0x1));// uint32
        // fc::unsigned_int
        writer.putVariableUInt(max_net_usage_words);
        writer.putVariableUInt(max_cpu_usage_ms);
        writer.putVariableUInt(delay_sec);

        writer.putCollection(context_free_actions);
        writer.putCollection(actions);
        //writer.putCollection(transaction_extensions);
        writer.putVariableUInt((long) transaction_extensions.size());
        long f = (long)(10000 * Utils.matchFloat(fee));
        TypeAsset asset = new TypeAsset(f);
        writer.putLongLE(asset.getAmount());
        writer.putLongLE(asset.assetSymbol());
        if (transaction_extensions.size() > 0) {
            // TODO 구체적 코드가 나오면 확인후 구현할 것.
        }
    }

    private Date getExpirationAsDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }

    }


    public Sha256 getDigestForSignature(TypeChainId chainId) {
        EosByteWriter writer = new EosByteWriter(255);

        // data layout to sign :
        // [ {chainId}, {Transaction( parent class )}, {hash of context_free_data only when exists ]

        writer.putBytes(chainId.getBytes());
        pack(writer);
        if (ctxFreeData != null && ctxFreeData.size() > 0){
        } else{
            writer.putBytes(Sha256.ZERO_HASH.getBytes());
        }
        return Sha256.from(writer.toBytes());
    }

}
