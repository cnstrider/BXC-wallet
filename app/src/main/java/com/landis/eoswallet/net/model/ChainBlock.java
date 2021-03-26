package com.landis.eoswallet.net.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChainBlock {
    public String timestamp;
    public int block_num;
    public long ref_block_prefix;

    public String getTimeAfterHeadBlockTime(int diffInMilSec){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{
            Date date = format.parse(this.timestamp);
            Calendar calendar =  Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MILLISECOND, diffInMilSec);
            date = calendar.getTime();
            return format.format(date);
        } catch (ParseException e) {

            return this.timestamp;
        }
    }
}
