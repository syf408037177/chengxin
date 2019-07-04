package com.chengxin.sync_job.biz.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.common.Constants;
import com.chengxin.sync_job.common.DataMapping;
import com.chengxin.sync_job.dao.erp.IPurchaseInDao;
import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import com.chengxin.sync_job.util.APICaller;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Song
 * @create 2019-05-06 16:07
 * @desc 采购入库biz
 **/
@Service
public class PurchaseInBizImpl implements IPurchaseInBiz {
    @Autowired
    private IPurchaseInDao purchaseInDao;

    @Autowired
    private DataMapping dataMapping;

    @Value("${dgType}")
    private String dgType;

    @Value("${purchase_in_count}")
    private Long purchaseInCount;

    @Value("${purchaseInPushIp}")
    private String ip;

    @Value("${purchaseInServiceName}")
    private String serviceName;

    private static Logger log = LoggerFactory.getLogger(PurchaseInBizImpl.class);


    /**
     * 处理数据变为传输json
     *
     * @param list
     * @return
     */
    private String transDataForJson(List<PurchaseInParentEntity> list, List<String> parentIds, List<String> checkFailIds) {
        try {
            JSONObject json = new JSONObject();
            JSONArray body = new JSONArray();
            for (PurchaseInParentEntity purchaseInParentEntity : list) {
                if (purchaseInParentEntity != null) {
                    JSONObject parentVO = new JSONObject();
                    if ("249".equals(dgType)) {
                        parentVO.put("cgeneralhid", "B" + purchaseInParentEntity.getUniqueid());
                        parentVO.put("vuserdef11", "9" + purchaseInParentEntity.getBillCode());
                    } else if ("247".equals(dgType)) {
                        parentVO.put("corderid", "A" + purchaseInParentEntity.getUniqueid());
                        parentVO.put("vuserdef11", "7" + purchaseInParentEntity.getBillCode());
                    }
                    parentVO.put("cdptid", purchaseInParentEntity.getCdptid());//部门ID
                    parentVO.put("cbilltypecode", "45");//库存单据类型编码[45为采购入库]
                    parentVO.put("cbizid", "1005A110000000000J10");//业务员ID
                    parentVO.put("cbiztype", "1005A110000000000009");//业务类型ID
                    parentVO.put("clastmodiid", "0001A11000000000066K");//最后修改人
                    parentVO.put("coperatorid", "0001A11000000000066K");//制单人
                    parentVO.put("cregister", "0001A11000000000066K");//库房签字人
                    try {
                        String date = purchaseInParentEntity.getOperateDate().split("\\s")[0];
                        parentVO.put("daccountdate", date);//订单时间
                        parentVO.put("dbilldate", date);//单据日期
                        parentVO.put("taccounttime", date);//库房签字时间
                        parentVO.put("tlastmoditime", date);//最后修改时间
                        parentVO.put("tmaketime", date);//制单时间
                    } catch (Exception e) {
                        parentVO.put("daccountdate", "");//订单时间
                        parentVO.put("dbilldate", "");//单据日期
                        parentVO.put("taccounttime", "");//库房签字时间
                        parentVO.put("tlastmoditime", "");//最后修改时间
                        parentVO.put("tmaketime", "");//制单时间
                        log.error("转换日期异常", e);
                    }
                    String pkCode = purchaseInParentEntity.getPkCode();//公司ID
                    parentVO.put("pk_corp", pkCode);//公司ID
                    String provId = null;
                    try {
                        provId = dataMapping.getProvId(purchaseInParentEntity.getProvName(), pkCode);
                    } catch (Exception e) {
                        log.error("该采购入库获取供应商失败将进行另一种匹配方法,单号:" + purchaseInParentEntity.getBillCode(), e);
                        provId = null;
                    }
                    if (StringUtils.isBlank(provId)) {
                        try {
                            provId = dataMapping.getProvId2(purchaseInParentEntity.getProvName(), pkCode);
                        } catch (Exception e) {
                            log.error("该采购入库找不到供应商失败,单号:" + purchaseInParentEntity.getBillCode());
                            provId = null;
                        }
                    }
                    if (StringUtils.isBlank(provId)) {
                        log.error("该采购入库找不到供应商,则不进行传输,单号:" + purchaseInParentEntity.getBillCode());
                        checkFailIds.add(purchaseInParentEntity.getUniqueid());
                        //如果找不到供应商id则这条数据不传送用友
                        continue;
                    }
                    parentVO.put("cproviderid", provId);//供应商id
                    String calbody = dataMapping.getCalbody(pkCode);//库存组织
                    String cubasdoc = dataMapping.getCubasdoc(purchaseInParentEntity.getProvName(), pkCode);//供应商基本档案ID
                    parentVO.put("pk_calbody", calbody);
                    parentVO.put("pk_cubasdoc", cubasdoc);
                    JSONArray childVO = new JSONArray();
                    List<PurchaseInChildEntity> childs = purchaseInParentEntity.getPurchaseInChilds();
                    String cbodywarehouseid = "";//库存仓库
                    if (childs != null && childs.size() > 0) {
                        for (PurchaseInChildEntity entity : childs) {
                            JSONObject child = new JSONObject();
                            child.put("cbodybilltypecode", "45");//单据类型
                            cbodywarehouseid = dataMapping.getCbodywarehouseid(entity.getWareCode(), pkCode);//库存仓库
                            child.put("cbodywarehouseid", cbodywarehouseid);
                            String cinvbasid = dataMapping.getCbaseid(entity.getMaterCode());//存货基本id
                            String cinventoryid = dataMapping.getCmangid(cinvbasid, pkCode);//存货ID
                            if ("249".equals(dgType)) {
                                child.put("cgeneralbid", "B" + entity.getUniqueid());//出入库单表体主键
                                child.put("cgeneralhid", "B" + entity.getMasterid());//出入库单表头主键
                                Map<String, String> firstBillId = dataMapping.getFirstBillId("9" + entity.getMissionCode(), cinvbasid);
                                child.put("cfirstbillbid", firstBillId.get("corder_bid"));//源头单据表体ID
                                child.put("cfirstbillhid", firstBillId.get("corderid"));//源头单据表头ID
                                child.put("csourcebillbid", firstBillId.get("corder_bid"));//来源单据表体序列号
                                child.put("csourcebillhid", firstBillId.get("corderid"));//来源单据表头序列号
                            } else if ("247".equals(dgType)) {
                                child.put("cgeneralbid", "A" + entity.getUniqueid());//出入库单表体主键
                                child.put("cgeneralhid", "A" + entity.getMasterid());//出入库单表头主键
                                Map<String, String> firstBillId = dataMapping.getFirstBillId("7" + entity.getMissionCode(), cinvbasid);
                                child.put("cfirstbillbid", firstBillId.get("corder_bid"));//源头单据表体ID
                                child.put("cfirstbillhid", firstBillId.get("corderid"));//源头单据表头ID
                                child.put("csourcebillbid", firstBillId.get("corder_bid"));//来源单据表体序列号
                                child.put("csourcebillhid", firstBillId.get("corderid"));//来源单据表头序列号
                            }
                            child.put("cinvbasid", cinvbasid);
                            child.put("cinventoryid", cinventoryid);
                            child.put("csourcetype", "23");//来源单据类型
                            child.put("cfirsttype", "21");//源头单据类型
                            child.put("cvendorid", provId);//供应商id
                            child.put("ninnum", entity.getPriceUnitAmt());//实入数量
                            child.put("nmny", entity.getItemMoney());//金额
                            child.put("nprice", entity.getAvePrice());//单价
                            child.put("pk_bodycalbody", calbody);//库存组织
                            child.put("pk_cubasdoc", cubasdoc);//客商基本档案ID
                            child.put("pk_corp", pkCode);//公司
                            child.put("pk_invoicecorp", pkCode);//收票公司
                            child.put("pk_reqcorp", pkCode);//需求公司
                            try {
                                String date = purchaseInParentEntity.getOperateDate().split("\\s")[0];
                                child.put("dbizdate", date);//业务日期
                            } catch (Exception e) {
                                child.put("dbizdate", "");//业务日期
                                log.error("转换日期异常", e);
                            }
                            childVO.add(child);
                        }
                    } else {
                        log.error("该采购入库单无明细,则不进行传输,单号:" + purchaseInParentEntity.getBillCode());
                        //如果没有子表数据则不传输
                        checkFailIds.add(purchaseInParentEntity.getUniqueid());
                        continue;
                    }
                    parentVO.put("cwarehouseid", cbodywarehouseid);//仓库id
                    JSONObject purchase = new JSONObject();
                    purchase.put("ParentVO", parentVO);
                    purchase.put("ChildrenVO", childVO);
                    body.add(purchase);
                    parentIds.add(purchaseInParentEntity.getUniqueid());
                }
            }
            json.put("GeneralBillVO_45", body);
            if(body != null && body.size() > 0) {
                return json.toJSONString();
            }
        } catch (Exception e) {
            log.error("处理采购入库单数据异常!", e);
        }
        //清空这些数据
        parentIds.clear();
        return null;
    }

    @Override
    public List<PurchaseInParentEntity> findPurchaseInNoTransfer() {
        try {
            return purchaseInDao.findPurchaseInParentNotTransfer(purchaseInCount, dgType);
        } catch (Exception e) {
            log.error("查询采购入库单数据异常!", e);
        }
        return null;
    }

    @Override
    public void pushPurchaseInToYy() {
        try {
            List<PurchaseInParentEntity> entities = findPurchaseInNoTransfer();
            if (entities != null && entities.size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                String json = transDataForJson(findPurchaseInNoTransfer(), pushIds, checkFailIds);
                log.info(Constants.PUSH_TAG + "本次需传输的采购入库单uniqueId:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.PUSH_TAG + "本次校验失败的采购入库单uniqueId:" + checkFailIds);
                    //将校验失败的单号置为2
                    purchaseInDao.updatePurchaseInStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.PUSH_TAG + "本次需要推送的采购入库单数据json:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.PUSH_TAG + "推送采购入库单数据成功!");
                            //成功推送修改采购入库单推送状态为成功
                            purchaseInDao.updatePurchaseInStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.PUSH_TAG + "推送采购入库单数据校验失败!具体失败原因:\n" + resultJson);
                            //推送修改采购入库单失败推送状态为失败
                            purchaseInDao.updatePurchaseInStatus(pushIds, Constants.PUSH_STATUS_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.PUSH_TAG + "推送采购入库单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.PUSH_TAG + "本次暂未查询到未传输的采购入库单数据");
            }
        } catch (Exception e) {
            log.error(Constants.PUSH_TAG + "推送采购入库单数据程序异常!", e);
        }
    }

    @Override
    public void reSyncFailOrder() {
        try {
            //查询传输的采购入库单数据
            PurchaseInParentEntity failData = purchaseInDao.findPurchaseInParentFail(dgType);
            if(failData != null && failData.getPurchaseInChilds() != null && failData.getPurchaseInChilds().size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                List<PurchaseInParentEntity> list = new ArrayList<PurchaseInParentEntity>();
                list.add(failData);
                String json = transDataForJson(list, pushIds, checkFailIds);
                log.info(Constants.RE_PUSH_TAG + "需要重推的采购入库单uniqueId:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.RE_PUSH_TAG + "重推校验失败的采购入库单uniqueId:" + checkFailIds);
                    //重推修改采购入库单失败推送状态为校验失败
                    purchaseInDao.updatePurchaseInStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.RE_PUSH_TAG + "需要重推的采购入库单数据json:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.RE_PUSH_TAG + "重推采购入库单数据成功!");
                            //成功推送修改采购入库单推送状态为成功
                            purchaseInDao.updatePurchaseInStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.RE_PUSH_TAG + "重推采购入库单数据校验失败!");
                            //重推修改采购入库单失败推送状态为校验失败
                            purchaseInDao.updatePurchaseInStatus(pushIds, Constants.CHECK_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.RE_PUSH_TAG + "重推采购入库单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.RE_PUSH_TAG + "本次暂未查询到传输失败的采购入库单数据");
            }
        } catch (Exception e) {
            log.error(Constants.RE_PUSH_TAG + "重推采购入库单数据程序异常!", e);
        }
    }

    @Override
    public void modifyOrderStatus() {
        try {
            purchaseInDao.modifyFailOrderStatus();
        } catch (Exception e) {
            log.error(Constants.MODIFY_TAG + "将校验失败的采购入库单号状态置为0异常!", e);
        }
    }
}
