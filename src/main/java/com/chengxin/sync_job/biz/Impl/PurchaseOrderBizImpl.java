package com.chengxin.sync_job.biz.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chengxin.sync_job.biz.IPurchaseOrderBiz;
import com.chengxin.sync_job.common.Constants;
import com.chengxin.sync_job.common.DataMapping;
import com.chengxin.sync_job.dao.erp.IPurchaseOrderDao;
import com.chengxin.sync_job.domain.PurchaseOrderChildEntity;
import com.chengxin.sync_job.domain.PurchaseOrderParentEntity;
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

/**
 * @author Mr.Song
 * @create 2019-05-06 16:07
 * @desc 采购入库biz
 **/
@Service
public class PurchaseOrderBizImpl implements IPurchaseOrderBiz {
    @Autowired
    private IPurchaseOrderDao purchaseOrderDao;

    @Autowired
    private DataMapping dataMapping;

    @Value("${dgType}")
    private String dgType;

    @Value("${purchase_order_count}")
    private Long purchaseOrderCount;

    @Value("${purchaseOrderPushIp}")
    private String ip;

    @Value("${purchaseOrderServiceName}")
    private String serviceName;

    private static Logger log = LoggerFactory.getLogger(PurchaseOrderBizImpl.class);


    /**
     * 处理数据变为传输json
     *
     * @param list
     * @return
     */
    private String transDataForJson(List<PurchaseOrderParentEntity> list, List<String> parentIds, List<String> checkFailIds) {
        try {
            JSONObject json = new JSONObject();
            JSONArray body = new JSONArray();
            for (PurchaseOrderParentEntity purchaseOrderParentEntity : list) {
                if (purchaseOrderParentEntity != null) {
                    JSONObject parentVO = new JSONObject();
                    if ("249".equals(dgType)) {
                        parentVO.put("corderid", "9" + purchaseOrderParentEntity.getOrderId());
                        parentVO.put("vdef9", "9" + purchaseOrderParentEntity.getBillId());
                    } else if ("247".equals(dgType)) {
                        parentVO.put("corderid", "7" + purchaseOrderParentEntity.getOrderId());
                        parentVO.put("vdef9", "7" + purchaseOrderParentEntity.getBillId());
                    }
                    parentVO.put("pk_corp", purchaseOrderParentEntity.getPkCorp());
                    parentVO.put("bislatest", "Y");
                    parentVO.put("cpurorganization", purchaseOrderParentEntity.getCpurOrganization());//采购组织
                    try {
                        String date = purchaseOrderParentEntity.getSetDate().split("\\s")[0];
                        parentVO.put("dorderdate", date);//订单时间
                        parentVO.put("tmaketime", date);//制单时间
                    } catch (Exception e) {
                        parentVO.put("dorderdate", "");//订单时间
                        parentVO.put("tmaketime", "");//制单时间
                        log.error("转换订单日期/制单日期异常", e);
                    }
                    String provId = null;
                    try {
                        provId = dataMapping.getProvId(purchaseOrderParentEntity.getProvName(), purchaseOrderParentEntity.getPkCorp());
                    } catch (Exception e) {
                        log.error("该采购订单获取供应商失败将进行另一种匹配方法,单号:" + purchaseOrderParentEntity.getOrderId(), e);
                        provId = null;
                    }
                    if (StringUtils.isBlank(provId)) {
                        try {
                            provId = dataMapping.getProvId2(purchaseOrderParentEntity.getProvName(), purchaseOrderParentEntity.getPkCorp());
                        } catch (Exception e) {
                            log.error("该采购订单找不到供应商失败,单号:" + purchaseOrderParentEntity.getOrderId());
                            provId = null;
                        }
                    }
                    if (StringUtils.isBlank(provId)) {
                        log.error("该采购订单找不到供应商,则不进行传输,单号:" + purchaseOrderParentEntity.getOrderId());
                        checkFailIds.add(purchaseOrderParentEntity.getOrderId());
                        //如果找不到供应商id则这条数据不传送用友
                        continue;
                    }
                    parentVO.put("cvendormangid", provId);
                    parentVO.put("cgiveinvoicevendor", provId);//发票方id
                    parentVO.put("cdeptid", purchaseOrderParentEntity.getCdeptId());
                    parentVO.put("coperator", "0001A11000000000066K");
                    parentVO.put("cbiztype", "1005A110000000000009");
                    JSONArray childVO = new JSONArray();
                    List<PurchaseOrderChildEntity> childs = purchaseOrderParentEntity.getPurchaseOrderChilds();
                    if (childs != null && childs.size() > 0) {
                        for (PurchaseOrderChildEntity entity : childs) {
                            JSONObject child = new JSONObject();
                            String cbaseid = null;
                            try {
                                cbaseid = dataMapping.getCbaseid(entity.getProdCode());
                            } catch (Exception e) {
                                log.error("该采购订单获取存货ID失败,则不进行传输,单号:" + purchaseOrderParentEntity.getOrderId());
                                checkFailIds.add(purchaseOrderParentEntity.getOrderId());
                                break;
                            }
                            child.put("cbaseid", cbaseid);
                            child.put("nordernum", entity.getPriceUnitQty());
                            child.put("norgtaxprice", entity.getPackPrice());
                            child.put("norgnettaxprice", entity.getPackPrice());
                            child.put("cassistunit", entity.getPackPriceUnit());
                            child.put("ndiscountrate", 10000);
                            child.put("coperator", "0001A11000000000066K");
                            child.put("pk_arrvcorp", purchaseOrderParentEntity.getPkCorp());
                            child.put("pk_invoicecorp", purchaseOrderParentEntity.getPkCorp());
                            child.put("pk_reqcorp", purchaseOrderParentEntity.getPkCorp());
                            String cmangid = dataMapping.getCmangid(cbaseid, purchaseOrderParentEntity.getPkCorp());
                            child.put("cmangid", cmangid);
                            childVO.add(child);
                        }
                    } else {
                        log.error("该采购订单无明细,则不进行传输,单号:" + purchaseOrderParentEntity.getOrderId());
                        //如果没有子表数据则不传输
                        checkFailIds.add(purchaseOrderParentEntity.getOrderId());
                        continue;
                    }
                    JSONObject purchase = new JSONObject();
                    purchase.put("parentvo", parentVO);
                    purchase.put("childrenvo", childVO);
                    body.add(purchase);
                    parentIds.add(purchaseOrderParentEntity.getOrderId());
                }
            }
            json.put("puordervo", body);
            if(body != null && body.size() > 0) {
                return json.toJSONString();
            }
        } catch (Exception e) {
            log.error("处理采购订单数据异常!", e);
        }
        //清空这些数据
        parentIds.clear();
        return null;
    }

    @Override
    public List<PurchaseOrderParentEntity> findPurchaseOrderNoTransfer() {
        try {
            return purchaseOrderDao.findPurchaseOrderParentNotTransfer(purchaseOrderCount, dgType);
        } catch (Exception e) {
            log.error("查询采购订单数据异常!", e);
        }
        return null;
    }

    @Override
    public void pushPurchaseOrderToYy() {
        try {
            List<PurchaseOrderParentEntity> entities = findPurchaseOrderNoTransfer();
            if (entities != null && entities.size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                String json = transDataForJson(findPurchaseOrderNoTransfer(), pushIds, checkFailIds);
                log.info(Constants.PUSH_TAG + "本次需传输的采购订单单号:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.PUSH_TAG + "本次校验失败的采购订单单号:" + checkFailIds);
                    //将校验失败的单号置为2
                    purchaseOrderDao.updatePurchaseOrderStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.PUSH_TAG + "本次需要推送的采购订单数据json:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.PUSH_TAG + "推送采购订单数据成功!");
                            //成功推送修改采购订单推送状态为成功
                            purchaseOrderDao.updatePurchaseOrderStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.PUSH_TAG + "推送采购订单数据校验失败!具体失败原因:\n" + resultJson);
                            //推送修改采购订单失败推送状态为失败
                            purchaseOrderDao.updatePurchaseOrderStatus(pushIds, Constants.PUSH_STATUS_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.PUSH_TAG + "推送采购订单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.PUSH_TAG + "本次暂未查询到未传输的采购订单数据");
            }
        } catch (Exception e) {
            log.error(Constants.PUSH_TAG + "推送采购订单数据程序异常!", e);
        }
    }

    @Override
    public void reSyncFailOrder() {
        try {
            //查询传输的采购订单数据
            PurchaseOrderParentEntity failData = purchaseOrderDao.findPurchaseOrderParentFail(dgType);
            if(failData != null && failData.getPurchaseOrderChilds() != null && failData.getPurchaseOrderChilds().size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                List<PurchaseOrderParentEntity> list = new ArrayList<PurchaseOrderParentEntity>();
                list.add(failData);
                String json = transDataForJson(list, pushIds, checkFailIds);
                log.info(Constants.RE_PUSH_TAG + "需要重推的采购订单单号:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.RE_PUSH_TAG + "重推校验失败的采购订单单号:" + checkFailIds);
                    //重推修改采购订单失败推送状态为校验失败
                    purchaseOrderDao.updatePurchaseOrderStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.RE_PUSH_TAG + "需要重推的采购订单数据josn:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.RE_PUSH_TAG + "重推采购订单数据成功!");
                            //成功推送修改采购订单推送状态为成功
                            purchaseOrderDao.updatePurchaseOrderStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.RE_PUSH_TAG + "重推采购订单数据校验失败!");
                            //重推修改采购订单失败推送状态为校验失败
                            purchaseOrderDao.updatePurchaseOrderStatus(pushIds, Constants.CHECK_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.RE_PUSH_TAG + "重推采购订单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.RE_PUSH_TAG + "本次暂未查询到传输失败的采购订单数据");
            }
        } catch (Exception e) {
            log.error(Constants.RE_PUSH_TAG + "重推采购订单数据程序异常!", e);
        }
    }

    @Override
    public void modifyOrderStatus() {
        try {
            purchaseOrderDao.modifyFailOrderStatus();
        } catch (Exception e) {
            log.error(Constants.MODIFY_TAG + "将校验失败的单号状态置为0异常!", e);
        }
    }
}
