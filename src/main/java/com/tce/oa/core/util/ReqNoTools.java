package com.tce.oa.core.util;

import java.util.Calendar;
import java.util.Random;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/30 16:00
 **/
public class ReqNoTools {

    /**
     *@Description 获取报销单号
     *@Date 16:01 2018/11/30
     *@Param [prefix]
     *@return java.lang.String
     **/
    public static String getReqNo(String prefix) {
        int length = 5;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<length; i++) {
            sb.append(random.nextInt(9));
        }
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        String month = String.valueOf(instance.get(Calendar.MONTH) + 1);
        String day = String.valueOf(instance.get(Calendar.DAY_OF_MONTH));
        return prefix + year + month + day + sb.toString();
    }

//    public static void main(String[] args) {
//        String reimburseNo = ReimburseTools.getReimburseNo("R");
//        System.out.println(reimburseNo);
//    }
}
