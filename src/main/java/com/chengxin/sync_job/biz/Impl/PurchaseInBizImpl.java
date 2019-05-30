package com.chengxin.sync_job.biz.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chengxin.sync_job.biz.IPurchaseInBiz;
import com.chengxin.sync_job.common.DataMapping;
import com.chengxin.sync_job.dao.erp.IPurchaseInDao;
import com.chengxin.sync_job.domain.PurchaseInChildEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<PurchaseInParentEntity> findPurchaseInNoTransfer() {
        return purchaseInDao.findPurchaseInParentNotTransfer();
    }

    /**
     * 处理数据变为传输json
     * @param list
     * @return
     */
    private String transDataForJson(List<PurchaseInParentEntity> list) {
        if(list != null && list.size() > 0) {
            JSONObject json = new JSONObject();
            JSONArray body = new JSONArray();
            for (PurchaseInParentEntity purchaseInParentEntity : list) {
                JSONObject parentVO = new JSONObject();
                parentVO.put("corderid", "");
                parentVO.put("cauditpsn", purchaseInParentEntity.getCreatorCode());
                parentVO.put("vordercode", "");
                parentVO.put("dorderdate", purchaseInParentEntity.getCreateDate());
                parentVO.put("tlastmaketime", purchaseInParentEntity.getOperateDate());
                parentVO.put("tmaketime", purchaseInParentEntity.getCreateDate());
                if(StringUtils.isNotBlank(purchaseInParentEntity.getCorpName())) {
                    parentVO.put("cpurorganization", dataMapping.getCompanyCode(purchaseInParentEntity.getCorpName()));
                }
                String providerCode = dataMapping.getProviderCode(purchaseInParentEntity.getSpecialCode());
                parentVO.put("cproviderid", providerCode);
                JSONArray childVO = new JSONArray();
                List<PurchaseInChildEntity> childs = purchaseInParentEntity.getPurchaseInChilds();
                if(childs != null && childs.size() > 0) {
                    for (PurchaseInChildEntity entity : childs) {
                        JSONObject child = new JSONObject();
                        if(StringUtils.isNotBlank(entity.getMaterCode())) {

                        }
                        child.put("cassistunit", entity.getMaterCode());
                        child.put("cbaseid  ", entity.getPriceUnitAmt());
                        child.put("nordernum", entity.getPriceUnitAmt());
                        childVO.add(child);
                    }
                }
                JSONObject purchase = new JSONObject();
                purchase.put("ParentVO", parentVO);
                purchase.put("ChildrenVO", childVO);
                body.add(purchase);
            }
//            JSONObject parentVO = new JSONObject();
//            parentVO.put("cbiztype", "8");
//            parentVO.put("corderid", "1001A1100000000020QV");
//            parentVO.put("coperatorid", "0001A1100000000002BE   ");
//            parentVO.put("cwarehouseid", "1001A110000000001EI3");
//            parentVO.put("cpurorganization", "0001A110000000004P5V");
//            JSONArray childVO = new JSONArray();
//            JSONObject child = new JSONObject();
//            child.put("cinventoryid", "0001A110000000003GWJ");
//            child.put("ninnum", 1);
//            child.put("nprice", 14.52503);
//            childVO.add(child);
//            JSONObject purchase = new JSONObject();
//            purchase.put("ParentVO", parentVO);
//            purchase.put("ChildrenVO", childVO);
//            body.add(purchase);
            json.put("GeneralBillVO", body);
            return json.toJSONString();
        }
        return null;
    }

    @Override
    public String getPurchase() {
        //查询未传输数据
        List<PurchaseInParentEntity> entities = findPurchaseInNoTransfer();
        //处理数据
        return transDataForJson(entities);
    }
}
