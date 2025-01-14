package cn.px.domain.vo;

import lombok.Data;

@Data
public class ObjectCountVo {

    /**
     * 状态
     */
    private  Integer status;

    /**
     * 数量
     */
    private  Long count;

    /**
     * 执行人
     */
    private  Long executor;
}
