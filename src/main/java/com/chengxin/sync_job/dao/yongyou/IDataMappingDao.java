package com.chengxin.sync_job.dao.yongyou;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("dataMappingDao")
@Mapper
public interface IDataMappingDao {

    /**
     * 供应商编码对照
     */
    public String getProviderCode(String erpProviderCode);

    /**
     * 客户代码对照
     */
    public String getCustomerCode(String erpProviderCode);

    /**
     * 公司编码对照
     * @param erpCompanyName
     * @return
     */
    public String getCompanyCode(String erpCompanyName);

    /**
     * 存货基本Id对照
     * @param erpProdCode
     * @return
     */
    public String getCbaseid(String erpProdCode);

    /**
     * 存货管理对照
     * @param cbaseId
     * @param pkCorp
     * @return
     */
    public String getCmangid(@Param("cbaseId") String cbaseId, @Param("pkCorp") String pkCorp);

    /**
     * 供应商id对照
     * @param provYName
     * @param pkCorp
     * @return
     */
    public String getProvId(@Param("provYName") String provYName, @Param("pkCorp") String pkCorp);
}
