package cn.px.quartz.task;

import cn.px.common.utils.StringUtils;
import cn.px.quartz.service.EhrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author 品讯科技
 */
@Component("reportworkTask")
public class ReportworkTask {
    private static final Logger log = LoggerFactory.getLogger(ReportworkTask.class);

    @Autowired
    EhrService ehrService;



    public void reportworkMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void reportworkParams(String params) {
    }

    public void reportworkNoParams() {
        System.out.println("执行无参方法");
    }

    // ehr部门同步
    public void zcDeptTask() {
        ehrService.syncDept();
    }

    // ehr人员同步
    public void zcUserTask() {
        ehrService.syncUser();
    }


}
