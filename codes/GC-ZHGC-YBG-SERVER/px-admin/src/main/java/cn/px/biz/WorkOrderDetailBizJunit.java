package cn.px.biz;


import javax.annotation.Resource;

import cn.px.domain.vo.MasterialsInfoReportVo;
import lombok.extern.slf4j.Slf4j;
import cn.px.domain.WorkOrderDetail;
import cn.px.biz.service.IWorkOrderDetailBiz;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 工单检测明细Biz 业务单元测试
 *
 * @author 品讯科技
 * @date 2024-08
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class WorkOrderDetailBizJunit
{

    @Resource
    private IWorkOrderDetailBiz workOrderDetailBiz;

	@Test
	public  void searchWorkOrderDetailList(){
	        log.info("进入testing searchWorkOrderDetailList()");
	        //TODO:
	        WorkOrderDetail workOrderDetail=new WorkOrderDetail();
	        workOrderDetailBiz.searchWorkOrderDetailList(workOrderDetail);
	}


	@Test
    public  void saveWorkOrderDetailVo(){
            log.info("进入testing saveWorkOrderDetailVo()");
            //TODO:
            WorkOrderDetail workOrderDetail=new WorkOrderDetail();
            workOrderDetailBiz.saveWorkOrderDetail(workOrderDetail);
    }

    @Test
    public  void test1(){
            log.info("进入testing test1() 物料明细报表查询");
        List<MasterialsInfoReportVo> reportVoList = workOrderDetailBiz.getMasterialsInfoReportVoList(null);
        log.info("size...."+reportVoList.size());

    }

 	@Test
    public  void test2(){
            log.info("进入testing test2() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public  void test3(){
			log.info("进入testing test3() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public  void test4(){
			log.info("进入testing test4() 不用感谢我给你初始化了一个测试方法");

    }

 	@Test
    public  void test5(){
    		log.info("进入testing test5() 不用感谢我给你初始化了一个测试方法");

    }

    @Test
    public  void test6(){
    		log.info("进入testing test6() 不用感谢我给你初始化了一个测试方法");

    }

}
