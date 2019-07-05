package com.chengxin.sync_job.biz.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chengxin.sync_job.biz.IArriveOrderBiz;
import com.chengxin.sync_job.common.Constants;
import com.chengxin.sync_job.common.DataMapping;
import com.chengxin.sync_job.dao.erp.IArriveOrderDao;
import com.chengxin.sync_job.domain.ArriveOrderChildEntity;
import com.chengxin.sync_job.domain.ArriveOrderParentEntity;
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
public class ArriveOrderBizImpl implements IArriveOrderBiz {
    @Autowired
    private IArriveOrderDao arriveOrderDao;

    @Autowired
    private DataMapping dataMapping;

    @Value("${dgType}")
    private String dgType;

    @Value("${arrive_order_count}")
    private Long arriveOrderCount;

    @Value("${arriveOrderPushIp}")
    private String ip;

    @Value("${arriveOrderServiceName}")
    private String serviceName;

    private static Logger log = LoggerFactory.getLogger(ArriveOrderBizImpl.class);


    /**
     * 处理数据变为传输json
     *
     * @param list
     * @return
     */
    private String transDataForJson(List<ArriveOrderParentEntity> list, List<String> parentIds, List<String> checkFailIds) {
        try {
            JSONObject json = new JSONObject();
            JSONArray body = new JSONArray();
            for (ArriveOrderParentEntity arriveOrderParentEntity : list) {
                if (arriveOrderParentEntity != null) {
                    JSONObject parentVO = new JSONObject();
                    if ("249".equals(dgType)) {
                        parentVO.put("carriveorderid", "9" + arriveOrderParentEntity.getCode());
                        parentVO.put("vdef11", "9" + arriveOrderParentEntity.getBillNo());
                    } else if ("247".equals(dgType)) {
                        parentVO.put("carriveorderid", "7" + arriveOrderParentEntity.getCode());
                        parentVO.put("vdef11", "7" + arriveOrderParentEntity.getBillNo());
                    }
                    parentVO.put("bisback", "N");//是否退货
                    parentVO.put("cauditpsn", "0001A11000000000066K");//审批人id
                    parentVO.put("cbilltype", "23");//单据类型
                    parentVO.put("cdeptid", arriveOrderParentEntity.getCdeptid());//采购部门ID
                    parentVO.put("cemployeeid", "1005A110000000000J10");//业务员ID
                    parentVO.put("coperator", "0001A11000000000066K");//制单人
                    String pkCode = arriveOrderParentEntity.getPkCode();//公司id
                    parentVO.put("cstoreorganization", dataMapping.getCalbody(pkCode));//库存组织ID
                    parentVO.put("cvendorbaseid", dataMapping.getCubasdoc(arriveOrderParentEntity.getProvName(), pkCode));//供应商基础ID
                    String provId = null;
                    try {
                        provId = dataMapping.getProvId(arriveOrderParentEntity.getProvName(), pkCode);
                    } catch (Exception e) {
                        log.error("该收货单获取供应商失败将进行另一种匹配方法,code为:" + arriveOrderParentEntity.getCode(), e);
                        provId = null;
                    }
                    if (StringUtils.isBlank(provId)) {
                        try {
                            provId = dataMapping.getProvId2(arriveOrderParentEntity.getProvName(), pkCode);
                        } catch (Exception e) {
                            log.error("该收货单找不到供应商失败,code为:" + arriveOrderParentEntity.getCode());
                            provId = null;
                        }
                    }
                    if (StringUtils.isBlank(provId)) {
                        log.error("该收货单找不到供应商,则不进行传输,code为:" + arriveOrderParentEntity.getCode());
                        checkFailIds.add(arriveOrderParentEntity.getCode());
                        //如果找不到供应商id则这条数据不传送用友
                        continue;
                    }
                    parentVO.put("cvendormangid", provId);//供应商管理ID
                    parentVO.put("ibillstatus", "2");//单据状态
                    parentVO.put("pk_corp", pkCode);//公司主键
                    parentVO.put("pk_purcorp", pkCode);//采购公司
                    String date = "";
                    try {
                        date = arriveOrderParentEntity.getReceiveDate().split("\\s")[0];//日期
                        String year = arriveOrderParentEntity.getReceiveDate().split("\\s")[0].split("-")[0];//年份
                        parentVO.put("dreceivedate", date);//到货日期
                        parentVO.put("caccountyear", year);//会计年度
                        parentVO.put("taudittime", date);//审批时间
                        parentVO.put("tlastmaketime", date);//最后修改时间
                        parentVO.put("tmaketime", date);//制单时间
                        parentVO.put("ts", date);
                    } catch (Exception e) {
                        parentVO.put("dreceivedate", "");//到货日期
                        parentVO.put("caccountyear", "");//会计年度
                        parentVO.put("taudittime", "");//审批时间
                        parentVO.put("tlastmaketime", "");//最后修改时间
                        parentVO.put("tmaketime", "");//制单时间
                        parentVO.put("ts", "");
                        log.error("转换日期异常", e);
                    }
                    JSONArray childVO = new JSONArray();
                    List<ArriveOrderChildEntity> childs = arriveOrderParentEntity.getArriveOrderChildEntities();
                    if (childs != null && childs.size() > 0) {
                        for (ArriveOrderChildEntity entity : childs) {
                            String cbaseid = dataMapping.getCbaseid(entity.getItemCode());//存货基础ID
                            JSONObject child = new JSONObject();
                            if ("249".equals(dgType)) {
                                child.put("carriveorderid", "9" + entity.getParentCode());//到货单ID
                                child.put("carriveorder_bid", "9" + entity.getCode());//到货单行ID
                                Map<String, String> firstBillId = dataMapping.getFirstBillId("9" + entity.getPurchaseNo(), cbaseid);
                                if(firstBillId == null) {
                                    log.error("该收货单找不到源头单据信息,不进行传输!code为:" + arriveOrderParentEntity.getCode());
                                    childVO.clear();
                                    break;
                                }
                                String corderid = firstBillId.get("corderid");
                                String corder_bid = firstBillId.get("corder_bid");
                                child.put("corderid", corderid);//采购订单ID
                                child.put("corder_bid", corder_bid);//源头单据表头ID
                                child.put("csourcebillid", corderid);//来源单据ID
                                child.put("csourcebillrowid", corder_bid);//来源单据行ID
                                child.put("cupsourcebillid", corderid);//上层来源单据ID
                                child.put("cupsourcebillrowid", corder_bid);//上层来源单据行ID
                            } else if ("247".equals(dgType)) {
                                child.put("carriveorderid", "7" + entity.getParentCode());//到货单ID
                                child.put("carriveorder_bid", "7" + entity.getCode());//到货单行ID
                                Map<String, String> firstBillId = dataMapping.getFirstBillId("7" + entity.getPurchaseNo(), cbaseid);
                                if(firstBillId == null) {
                                    log.error("该收货单找不到源头单据信息,不进行传输!code为:" + arriveOrderParentEntity.getCode());
                                    childVO.clear();
                                    break;
                                }
                                String corderid = firstBillId.get("corderid");
                                String corder_bid = firstBillId.get("corder_bid");
                                child.put("corderid", corderid);//采购订单ID
                                child.put("corder_bid", corder_bid);//源头单据表头ID
                                child.put("csourcebillid", corderid);//来源单据ID
                                child.put("csourcebillrowid", corder_bid);//来源单据行ID
                                child.put("cupsourcebillid", corderid);//上层来源单据ID
                                child.put("cupsourcebillrowid", corder_bid);//上层来源单据行ID
                            }
                            child.put("cbaseid", cbaseid);
                            child.put("ccurrencytypeid", "00010000000000000001");//原币币种ID
                            child.put("cmangid", dataMapping.getCmangid(cbaseid, pkCode));//存货管理ID
                            child.put("csourcebilltype", "21");//来源单据类型
                            child.put("cupsourcebilltype", "21");//上层单据类型
                            child.put("narrvnum", entity.getPriceUnitAmt());//到货数量
                            child.put("pk_corp", pkCode);//公司主键
                            child.put("pk_invoicecorp", entity.getPkCorp());//收票公司
                            child.put("pk_reqcorp", entity.getPkCorp());//收票公司
                            child.put("ts", date);
                            childVO.add(child);
                        }
                    } else {
                        log.error("该收货单无明细,则不进行传输,code为:" + arriveOrderParentEntity.getCode());
                        //如果没有子表数据则不传输
                        checkFailIds.add(arriveOrderParentEntity.getCode());
                        continue;
                    }
                    if(childVO.size() == 0) {
                        log.error("收货单子表校验未通过则不进行传输,code为:" + arriveOrderParentEntity.getCode());
                        checkFailIds.add(arriveOrderParentEntity.getCode());
                        continue;
                    }
                    JSONObject arriveOrder = new JSONObject();
                    arriveOrder.put("parentvo", parentVO);
                    arriveOrder.put("childrenvo", childVO);
                    body.add(arriveOrder);
                    parentIds.add(arriveOrderParentEntity.getCode());
                }
            }
            json.put("arriveordervo", body);
            if(body != null && body.size() > 0) {
                return json.toJSONString();
            }
        } catch (Exception e) {
            log.error("处理收货单数据异常!", e);
        }
        //清空这些数据
        parentIds.clear();
        return null;
    }

    @Override
    public List<ArriveOrderParentEntity> findArriveOrderNoTransfer() {
        try {
            return arriveOrderDao.findArriveOrderParentNotTransfer(arriveOrderCount, dgType);
        } catch (Exception e) {
            log.error("查询收货单数据异常!", e);
        }
        return null;
    }

    @Override
    public void pushArriveOrderToYy() {
        try {
            List<ArriveOrderParentEntity> entities = findArriveOrderNoTransfer();
            if (entities != null && entities.size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                String json = transDataForJson(entities, pushIds, checkFailIds);
                log.info(Constants.PUSH_TAG + "本次需传输的收货单code为:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.PUSH_TAG + "本次校验失败的收货单code为:" + checkFailIds);
                    //将校验失败的单号置为2
                    arriveOrderDao.updateArriveOrderStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.PUSH_TAG + "本次需要推送的收货单数据json:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.PUSH_TAG + "推送收货单数据成功!");
                            //成功推送修改收货单推送状态为成功
                            arriveOrderDao.updateArriveOrderStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.PUSH_TAG + "推送收货单数据校验失败!具体失败原因:\n" + resultJson);
                            //推送修改收货单失败推送状态为失败
                            arriveOrderDao.updateArriveOrderStatus(pushIds, Constants.PUSH_STATUS_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.PUSH_TAG + "推送收货单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.PUSH_TAG + "本次暂未查询到未传输的收货单数据");
            }
        } catch (Exception e) {
            log.error(Constants.PUSH_TAG + "推送收货单数据程序异常!", e);
        }
    }

    @Override
    public void reSyncFailOrder() {
        try {
            //查询传输的收货单数据
            ArriveOrderParentEntity failData = arriveOrderDao.findArriveOrderParentFail(dgType);
            if(failData != null && failData.getArriveOrderChildEntities() != null && failData.getArriveOrderChildEntities().size() > 0) {
                List<String> pushIds = new ArrayList<String>(); //需要传输的单据id
                List<String> checkFailIds = new ArrayList<String>(); //校验不通过的单据id
                List<ArriveOrderParentEntity> list = new ArrayList<ArriveOrderParentEntity>();
                list.add(failData);
                String json = transDataForJson(list, pushIds, checkFailIds);
                log.info(Constants.RE_PUSH_TAG + "需要重推的收货单uniqueId:" + pushIds);
                if(checkFailIds.size() > 0) {
                    log.info(Constants.RE_PUSH_TAG + "重推校验失败的收货单uniqueId:" + checkFailIds);
                    //重推修改收货单失败推送状态为校验失败
                    arriveOrderDao.updateArriveOrderStatus(checkFailIds, Constants.CHECK_FAIL);
                }
                log.info(Constants.RE_PUSH_TAG + "需要重推的收货单数据json:\n" + json);
                if(json != null) {
                    try {
                        String result = APICaller.call(ip, serviceName, "pk", json);
                        JSONObject resultJson = JSON.parseObject(result);
                        String status = resultJson.getString("status");
                        if ("success".equals(status)) {
                            log.info(Constants.RE_PUSH_TAG + "重推收货单数据成功!");
                            //成功推送修改收货单推送状态为成功
                            arriveOrderDao.updateArriveOrderStatus(pushIds, Constants.PUSH_STATUS_SUCCESS);
                        } else {
                            log.error(Constants.RE_PUSH_TAG + "重推收货单数据校验失败!具体失败原因:\n" + resultJson);
                            //重推修改收货单失败推送状态为校验失败
                            arriveOrderDao.updateArriveOrderStatus(pushIds, Constants.CHECK_FAIL);
                        }
                    } catch (IOException e) {
                        log.error(Constants.RE_PUSH_TAG + "重推收货单数据接口异常!", e);
                    }
                }
            } else {
                log.info(Constants.RE_PUSH_TAG + "本次暂未查询到传输失败的收货单数据");
            }
        } catch (Exception e) {
            log.error(Constants.RE_PUSH_TAG + "重推收货单数据程序异常!", e);
        }
    }

    @Override
    public void modifyOrderStatus() {
        try {
            arriveOrderDao.modifyFailOrderStatus();
        } catch (Exception e) {
            log.error(Constants.MODIFY_TAG + "将校验失败的收货单号状态置为0异常!", e);
        }
    }
}
