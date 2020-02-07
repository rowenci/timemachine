package com.rowenci.timemachine;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.service.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class TimemachineApplicationTests {

    @Resource
    private IMessageService iMessageService;

    @Test
    void contextLoads() {
    }

    @Test
    void ttt(){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", 1);
        IPage<Message> messageIPage1 = new Page<>(1, 5);
        IPage<Message> messageIPage2 = new Page<>(2, 5);

        List<Message> messageList1 = iMessageService.page(messageIPage1, qw).getRecords();
        List<Message> messageList2 = iMessageService.page(messageIPage2, qw).getRecords();
        System.out.println(messageList1);
        System.out.println(messageList2);
    }

}
