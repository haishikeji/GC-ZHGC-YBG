package cn.px.biz;


import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.px.domain.Routing;
import cn.px.biz.service.IRoutingBiz;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * 基础数据-工艺路线Biz 业务单元测试
 *
 * @author 品讯科技
 * @date 2024-08
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class RoutingBizJunit
{

    @Resource
    private IRoutingBiz routingBiz;

	@Test
	public  void searchRoutingList(){
	        log.info("进入testing searchRoutingList()");
	        //TODO:
	        Routing routing=new Routing();
	        routingBiz.searchRoutingList(routing);
	}


	@Test
    public  void saveRoutingVo(){
            log.info("进入testing saveRoutingVo()");
            //TODO:
            Routing routing=new Routing();
            routingBiz.saveRouting(routing);
    }

    /**
     * 生成产品工艺路线信息
     */
    @Test
    public  void saveRoutingListTest(){
            log.info("进入testing saveRoutingListTest() 保存工艺路线");

            routingBiz.saveRoutingList();
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
