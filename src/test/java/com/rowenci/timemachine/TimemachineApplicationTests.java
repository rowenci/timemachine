package com.rowenci.timemachine;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rowenci.timemachine.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class TimemachineApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void dataBaseTest() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", 1);
        System.out.println(userMapper.selectOne(queryWrapper));
    }

}
