package cn.px.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.px.domain.Stock;
import cn.px.domain.request.CountTypeReqVo;
import cn.px.domain.vo.StockVo;

/**
 * 仓储管理-出库Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface StockMapper
{
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

    public List<StockVo> selectStockVoList(StockVo stock);

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
     * 删除仓储管理-出库
     *
     * @param id 仓储管理-出库主键
     * @return 结果
     */
    public int deleteStockById(Long id);

    /**
     * 批量删除仓储管理-出库
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockByIds(Long[] ids);

    public int  selectStorageStockCount(CountTypeReqVo typeReqVo);

    List<Map<String, BigDecimal>> getStockTypeProportion(Long tenantId);


    List<Map<String, Object>> selectStockCountTopTrendList(Long tenantId);

    public List<Stock> stockTop20list(Long tenantId);

    public List<Stock> selectStockListByOrderNumber(Stock stock);

    StockVo selectStock(Stock stock);

}
