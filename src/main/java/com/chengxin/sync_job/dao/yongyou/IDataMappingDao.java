package com.chengxin.sync_job.dao.yongyou;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

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

    /**
     * 供应商id对照方法2
     * @param provYName
     * @param pkCorp
     * @return
     */
    public String getProvId2(@Param("provYName") String provYName, @Param("pkCorp") String pkCorp);

    /**
     * 库存组织对照
     * @param erpOwnerCorp
     * @return
     */
    public String getCalbody(String erpOwnerCorp);

    /**
     * 供应商基本档案ID对照
     * @param erpProvName
     * @return
     */
    public String getCubasdoc(@Param("erpProvName") String erpProvName, @Param("pkCode") String pkCode);

    /**
     * 库存仓库对照
     * @param erpWareCode
     * @param pkCode
     * @return
     */
    public String getCbodywarehouseid(@Param("erpWareCode") String erpWareCode, @Param("pkCode") String pkCode);

    /**
     * 源头单据id对照
     * @param missionCode
     * @return
     */
    public Map<String, String> getFirstBillId(@Param("missionCode") String missionCode, @Param("cbaseid") String cbaseId);
}
