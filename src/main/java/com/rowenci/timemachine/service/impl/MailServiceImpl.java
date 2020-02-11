package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.mapper.MessageMapper;
import com.rowenci.timemachine.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/11 15:01
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendHtmlMail(String to, String title, String context) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //发件人
            messageHelper.setFrom(from);
            //收件人
            messageHelper.setTo(to);
            //主题
            message.setSubject(title);
            //内容
            messageHelper.setText(context);
            //发送
            mailSender.send(message);
            log.info("邮件已经发送");
        }catch (MessagingException e){
            log.info("邮件发送异常 ", e);
        }
    }

}
