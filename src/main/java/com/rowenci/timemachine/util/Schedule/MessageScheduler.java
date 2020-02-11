package com.rowenci.timemachine.util.Schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.User;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IUserService;
import com.rowenci.timemachine.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 核心功能 每天定时发送邮件
 * </p>
 *
 * @author rowenci
 * @since 2020/2/11 14:27
 */

@Component
@Slf4j
public class MessageScheduler {

    @Resource
    private IMessageService iMessageService;

    @Resource
    private IUserService iUserService;

    @Resource
    private MailService mailService;

    /**
     * 生日祝福
     */
    @Scheduled(cron = "0 0 0 * * ?")      //每天0点AM
    public void birthdayJob() {
        /*Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("birthday", today);*/
        List<User> userList = iUserService.list();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(date);
            try {
                //将日期转成Date对象作比较
                Date fomatDate1 = sdf.parse(dateNow);
                Date fomatDate2 = sdf.parse(user.getBirthday());
                //比较两个日期
                int result = fomatDate2.compareTo(fomatDate1);
                if (result == 0) {
                    //到了日期
                    mailService.sendHtmlMail(user.getEmail(), "生日祝福", "生日快乐 " + user.getUserId());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 信件发送
     */
    @Scheduled(cron = "0 0 3 * * ?")      //每天3点AM
    public void messageJob() {
        //查询未发送的邮件
        QueryWrapper qw = new QueryWrapper();
        qw.eq("is_send", 0);
        List<Message> messageList = iMessageService.list(qw);
        System.out.println(messageList);
        for (int i = 0; i < messageList.size(); i++) {
            Message message = messageList.get(i);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(date);
            try {
                //将日期转成Date对象作比较
                Date fomatDate1 = sdf.parse(dateNow);
                Date fomatDate2 = sdf.parse(message.getSendTime());
                //比较两个日期
                int result = fomatDate2.compareTo(fomatDate1);
                if (result == 0) {
                    //到了日期
                    System.out.println(message);
                    mailService.sendHtmlMail(message.getTarget(), message.getTitle(), message.getContext());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //更新发送状态
            UpdateWrapper uw = new UpdateWrapper();
            uw.eq("message_id", message.getMessageId());
            uw.set("is_send", 1);
            iMessageService.update(uw);
        }
    }

}
