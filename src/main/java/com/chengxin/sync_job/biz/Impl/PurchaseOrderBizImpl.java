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
    private String transDataForJson(List<PurchaseOrderParentEntity> list) {
        try {
            JSONObject json = new JSONObject();
            JSONArray body = new JSONArray();
            for (PurchaseOrderParentEntity purchaseOrderParentEntity : list) {
                if (purchaseOrderParentEntity != null) {
                    JSONObject parentVO = new JSONObject();
                    if ("249".equals(dgType)) {
                        parentVO.put("corderid", "9" + purchaseOrderParentEntity.getOrderId());
                        parentVO.put("pk_defdoc11", "9" + purchaseOrderParentEntity.getBillId());
                    } else if ("247".equals(dgType)) {
                        parentVO.put("corderid", "7" + purchaseOrderParentEntity.getOrderId());
                        parentVO.put("pk_defdoc11", "7" + purchaseOrderParentEntity.getBillId());
                    }
                    parentVO.put("pk_corp", purchaseOrderParentEntity.getPkCorp());
                    parentVO.put("bislatest", "Y");
                    parentVO.put("cpurorganization", purchaseOrderParentEntity.getCpurOrganization());//采购组织
                    String setDate = purchaseOrderParentEntity.getSetDate();
                    try {
                        String date = purchaseOrderParentEntity.getSetDate().split("\\s")[0];
                        parentVO.put("dorderdate", date);//订单时间
                        parentVO.put("tmaketime", date);//制单时间
                    } catch (Exception e) {
                        parentVO.put("dorderdate", "");//订单时间
                        parentVO.put("tmaketime", "");//制单时间
                        log.error("转换订单日期/制单日期异常", e);
                    }
                    String provId = dataMapping.getProvId(purchaseOrderParentEntity.getProvName(), purchaseOrderParentEntity.getPkCorp());
                    if (StringUtils.isBlank(provId)) {
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
                            String cbaseid = dataMapping.getCbaseid(entity.getProdCode());
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
                        //如果没有子表数据则不传输
                        continue;
                    }
                    JSONObject purchase = new JSONObject();
                    purchase.put("parentvo", parentVO);
                    purchase.put("childrenvo", childVO);
                    body.add(purchase);
                }
            }
            json.put("puordervo", body);
            if(body != null && body.size() > 0) {
                return json.toJSONString();
            }
        } catch (Exception e) {
            log.error("处理采购订单数据异常!", e);
        }
        return null;
    }

    @Override
    public List<PurchaseOrderParentEntity> findPurchaseOrderNoTransfer() {
        try {
            return purchaseOrderDao.findPurchaseOrderParentNotTransfer(purchaseOrderCount);
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
                List<String> parentIds = new ArrayList<String>();
                for (PurchaseOrderParentEntity entity : entities) {
                    if (entity != null && entity.getPurchaseOrderChilds() != null && entity.getPurchaseOrderChilds().size() > 0) {
                        parentIds.add(entity.getOrderId());
                    }
                }
                String json = transDataForJson(findPurchaseOrderNoTransfer());
                if(json != null) {
                    log.info("本次需要推送的采购订单数据josn:\n" + json);
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJosn = JSON.parseObject(result);
                        String status = resultJosn.getString("status");
                        if ("success".equals(status)) {
                            log.info("推送采购订单数据成功!");
                            //成功推送修改采购订单推送状态为成功
                            purchaseOrderDao.updatePurchaseOrderStatus(parentIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error("推送采购订单数据校验失败!");
                            //推送修改采购订单失败推送状态为失败
                            purchaseOrderDao.updatePurchaseOrderStatus(parentIds, Constants.PUSH_STATUS_FAIL);
                        }
                    } catch (IOException e) {
                        log.error("推送采购订单数据接口异常!", e);
                    }
                }
            } else {
                log.info("本次暂未查询到未传输的采购订单数据");
            }
        } catch (Exception e) {
            log.error("推送采购订单数据程序异常!", e);
        }
    }

    @Override
    public void reSyncFailOrder() {
        try {
            //查询传输的采购订单数据
            PurchaseOrderParentEntity failData = purchaseOrderDao.findPurchaseOrderParentFail();
            if(failData != null && failData.getPurchaseOrderChilds() != null && failData.getPurchaseOrderChilds().size() > 0) {
                List<String> parentIds = new ArrayList<String>();
                parentIds.add(failData.getOrderId());
                List<PurchaseOrderParentEntity> list = new ArrayList<PurchaseOrderParentEntity>();
                list.add(failData);
                String json = transDataForJson(list);
                if(json != null) {
                    log.info("本次需要推送的采购订单数据josn:\n" + json);
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJosn = JSON.parseObject(result);
                        String status = resultJosn.getString("status");
                        if ("success".equals(status)) {
                            log.info("推送采购订单数据成功!");
                            //成功推送修改采购订单推送状态为成功
                            purchaseOrderDao.updatePurchaseOrderStatus(parentIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error("重推采购订单数据校验失败!");
                            //重推修改采购订单失败推送状态为重推失败
                            purchaseOrderDao.updatePurchaseOrderStatus(parentIds, Constants.REPUSH_STATUS_FAIL);
                        }
                    } catch (IOException e) {
                        log.error("推送采购订单数据接口异常!", e);
                    }
                }
            } else {
                log.info("本次暂未查询到传输失败的采购订单数据");
            }
        } catch (Exception e) {
            log.error("重推采购订单数据程序异常!", e);
        }
    }

}
