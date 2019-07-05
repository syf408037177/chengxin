package com.chengxin.sync_job.dao.erp;

import com.chengxin.sync_job.domain.ArriveOrderChildEntity;
import com.chengxin.sync_job.domain.ArriveOrderParentEntity;
import com.chengxin.sync_job.domain.PurchaseInParentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("arriveOrderDao")
@Mapper
public interface IArriveOrderDao {

    /**
     * 查询未传输至用友的收货单数据
     * @return
     */
    public List<ArriveOrderParentEntity> findArriveOrderParentNotTransfer(@Param("arriveOrderCount") Long purchaseInCount, @Param("dgType") String dgType);

    /**
     * 查询传输失败的收货单数据
     * @return
     */
    public ArriveOrderParentEntity  findArriveOrderParentFail(@Param("dgType") String dgType);

    /**
     * 根据主表id查询子表数据
     * @param parentId
     * @return
     */
    public List<ArriveOrderChildEntity> findArriveOrderChildByParentId(String parentId);

    /**
     * 修改收货单推送状态
     * @param parentIds
     * @param yyFinishFlag
     * @return
     */
    public Integer updateArriveOrderStatus(@Param("parentIds") List<String> parentIds, @Param("yyFinishFlag") String yyFinishFlag);

    /**
     * 将校验失败的单号状态置为0(可继续传输)
     * @return
     */
    public Integer modifyFailOrderStatus();
}
