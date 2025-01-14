package cn.px.biz;


import cn.px.biz.service.ICheckBiz;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 仓储管理-检测Biz 业务单元测试
 *
 * @author 品讯科技
 * @date 2024-08
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CheckBizJunit {

    @Resource
    private ICheckBiz checkBiz;

    /**
     * 查询检测列表信息
     */
    @Test
    public void searchCheckList() {
        log.info("进入testing searchStockList()");
        //TODO:
        checkBiz.searchCheckList();
    }

    /**
     * 保存检测信息
     */
    @Test
    public void saveCheckVo() {
        log.info("进入testing saveStockVo()");
        //TODO:
        checkBiz.saveCheck();
    }

    @Test
    public void test1() {
        log.info("进入testing test1() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public void test2() {
        log.info("进入testing test2() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public void test3() {
        log.info("进入testing test3() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public void test4() {
        log.info("进入testing test4() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public void test5() {
        log.info("进入testing test5() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public void test6() {
        log.info("进入testing test6() 不用感谢我给你初始化了一个测试方法");

    }

}
