package com.zgsu.graduation;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
class GraduationApplicationTests {

    public void init(){
        System.out.println("开始测试-----------");
    }

    public void after(){
        System.out.println("测试结束-------");
    }

    @Test
    void contextLoads() {
    }

}
