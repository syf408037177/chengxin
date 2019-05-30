package com.chengxin.sync_job.util;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestSaleOrderInsert {


    public static void main(String[] args) {
        testInsert();
    }

    public static void testInsert() {
        JSONObject json = new JSONObject();
        JSONArray bills = new JSONArray();
        JSONObject bill = new JSONObject();
        JSONObject head = new JSONObject();
        head.put("cbiztype", "8");
        head.put("coperatorid", "11310202");
        head.put("cwarehouseid", "ck01");
        head.put("pk_calbody", "000");
        head.put("pk_corp", "000");
        head.put("dbilldate", "2019-5-21 11:30:00");
        JSONArray bodys = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("cinventoryid", "150101/101092196551-CQ-0");
        body.put("cspaceid", "17-01-01-8");
        body.put("ninnum", "1");
        body.put("ninspacenum", "1");
        body.put("nprice", "8");
        bodys.add(body);
        bill.put("ParentVO", head);
        bill.put("ChildrenVO", bodys);
        bills.add(bill);
        json.put("GeneralBillVO_45", bills);
        System.out.println(json);
        APICaller caller = new APICaller();
        try {
            String result = caller.call("http://223.95.171.130:9000", "ic/purchasein/insert", "code", json.toString());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
