package com.chengxin.sync_job.common;

import com.chengxin.sync_job.dao.yongyou.IDataMappingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Song
 * @create 2019-05-21 15:12
 * @desc 数据对照处理类
 **/
@Component
public class DataMapping {
    @Autowired
    private IDataMappingDao dataMappingDao;

    /**
     * 供应商编码对照
     */
    public String getProviderCode(String erpProviderCode) {
        return dataMappingDao.getProviderCode(erpProviderCode);
    }

    /**
     * 客户代码对照
     */
    public String getCustomerCode(String erpProviderCode) {
        return null;
    }

    /**
     * 公司编码对照
     * @param erpCompanyName
     * @return
     */
    public String getCompanyCode(String erpCompanyName) {
        return dataMappingDao.getProviderCode(erpCompanyName);
    }

    /**
     * 存货基本Id对照
     * @param erpProdCode
     * @return
     */
    public String getCbaseid(String erpProdCode) {
        return dataMappingDao.getCbaseid(erpProdCode);
    }

    /**
     * 存货管理对照
     * @param cbaseId
     * @param pkCorp
     * @return
     */
    public String getCmangid(String cbaseId, String pkCorp) {
        return dataMappingDao.getCmangid(cbaseId, pkCorp);
    }

    /**
     * 供应商id对照
     * @param provYName
     * @return
     */
    public String getProvId(String provYName, String pkCorp) {
        return dataMappingDao.getProvId(provYName, pkCorp);
    }
}
