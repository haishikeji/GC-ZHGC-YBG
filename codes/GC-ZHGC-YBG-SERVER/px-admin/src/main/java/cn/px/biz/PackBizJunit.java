package cn.px.biz;


import javax.annotation.Resource;

import cn.px.domain.Materials;
import lombok.extern.slf4j.Slf4j;
import cn.px.domain.Pack;
import cn.px.biz.service.IPackBiz;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * 基础数据-包装Biz 业务单元测试
 *
 * @author 品讯科技
 * @date 2024-08
 */
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class PackBizJunit
{

    @Resource
    private IPackBiz packBiz;

	@Test
	public  void searchPackList(){
	        log.info("进入testing searchPackList()");
	        //TODO:
	        Pack pack=new Pack();
            pack.setName("弹簧7985");
            pack.setTenantId(1783048232520253440L);
	        packBiz.searchPackList(pack);
	}


	@Test
    public  void savePackVo(){
            log.info("进入testing savePackVo()");
            //TODO:
            Pack pack=new Pack();
            packBiz.savePack(pack);
    }

    /**
     * 插入包装基础数据
     */
    @Test
    public  void testSavePackLists(){
            log.info("进入testing testSavePackLists() 不用感谢我给你初始化了一个测试方法");
            packBiz.insertPacks(new Materials());

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
