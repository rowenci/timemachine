package com.rowenci.timemachine;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class TimemachineApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void email(){
    }

}
