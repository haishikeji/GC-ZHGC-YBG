package cn.px.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.px.common.core.domain.AjaxResult;
import cn.px.domain.Stock;
import cn.px.domain.request.CountTypeReqVo;
import cn.px.domain.vo.OutboundVo;
import cn.px.domain.vo.StockVo;

/**
 * 仓储管理-出库Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IStockService {
    /**
     * 查询仓储管理-出库
     *
     * @param id 仓储管理-出库主键
     * @return 仓储管理-出库
     */
    public Stock selectStockById(Long id);


    /**
     * 查询仓储管理-出库列表
     *
     * @param stock 仓储管理-出库
     * @return 仓储管理-出库集合
     */
    public List<Stock> selectStockList(Stock stock);

    public List<StockVo> selectStockVoList(StockVo stockVo);

    /**
     * 新增仓储管理-出库
     *
     * @param stock 仓储管理-出库
     * @return 结果
     */
    public int insertStock(Stock stock);

    /**
     * 修改仓储管理-出库
     *
     * @param stock 仓储管理-出库
     * @return 结果
     */
    public int updateStock(Stock stock);

    /**
     * 批量删除仓储管理-出库
     *
     * @param ids 需要删除的仓储管理-出库主键集合
     * @return 结果
     */
    public int deleteStockByIds(Long[] ids);

    public List<Stock> selectStockListByOrderNumber(String orderNumber);

    /**
     * 删除仓储管理-出库信息
     *
     * @param id 仓储管理-出库主键
     * @return 结果
     */
    public int deleteStockById(Long id);

    public int selectStorageStockCount(CountTypeReqVo typeReqVo);

    List<Map<String, BigDecimal>> getStockTypeProportion(Long tenantId);

    List<Map<String, Object>> selectStockCountTopTrendList(Long tenantId);

    /**
     * 订单出库
     * @param outboundVo
     * @return
     */
    public AjaxResult saveStockOut(OutboundVo outboundVo);

    /**
     * 采购订单退货出库
     * @param outboundVo
     * @return
     */
    public AjaxResult saveReturnStockOut(OutboundVo outboundVo);

    /**
     * 其它需求出库
     * @param outboundVo
     * @return
     */
    public AjaxResult saveInventoryStockOut(OutboundVo outboundVo);

    public List<Stock> stockTop20list(Long tenantId);

    /**
     * 多条件查询出库记录
     * @param stock
     * @return
     */
    public StockVo selectStock(Stock stock);
}
