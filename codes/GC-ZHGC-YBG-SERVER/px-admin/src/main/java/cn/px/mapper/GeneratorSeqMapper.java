package cn.px.mapper;

import java.util.List;
import cn.px.domain.GeneratorSeq;

/**
 * 流水号生成器Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface GeneratorSeqMapper
{
    /**
     * 获取流水号生成器自增id值
     * @param
     * @return
     */
    public Long getIdAutoSeq(String  schema);

    /**
     * 查询流水号生成器
     *
     * @param id 流水号生成器主键
     * @return 流水号生成器
     */
    public GeneratorSeq selectGeneratorSeqById(Long id);

    /**
     * 查询流水号生成器列表
     *
     * @param generatorSeq 流水号生成器
     * @return 流水号生成器集合
     */
    public List<GeneratorSeq> selectGeneratorSeqList(GeneratorSeq generatorSeq);


    /**
     * 新增流水号生成器
     *
     * @param generatorSeq 流水号生成器
     * @return 结果
     */
    public int insertGeneratorSeq(GeneratorSeq generatorSeq);

    /**
     * 修改流水号生成器
     *
     * @param generatorSeq 流水号生成器
     * @return 结果
     */
    public int updateGeneratorSeq(GeneratorSeq generatorSeq);

    /**
     * 删除流水号生成器
     *
     * @param id 流水号生成器主键
     * @return 结果
     */
    public int deleteGeneratorSeqById(Long id);

    /**
     * 批量删除流水号生成器
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGeneratorSeqByIds(Long[] ids);

    /**
     * 查询流水号生成器
     *
     * @param id 流水号生成器主键
     * @return 流水号生成器
     */
    public GeneratorSeq selectGeneratorSeqByCode(GeneratorSeq generatorSeq);
}
