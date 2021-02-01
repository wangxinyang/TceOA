package com.tce.oa.core.common.handler;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.constant.state.ProcessNameType;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 采购申请表 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-12-17
 */
@Component
public class MailSendHandler {

    private static Logger logger = LoggerFactory.getLogger(MailSendHandler.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${guns.oa-url}")
    private String oa_url;

    @Async
    public void sendTaskMail(String assignee, String reqno, String usrname, Integer type, Date date) {
        // 简单做 流程到下一个之后发送邮件通知
        String email = ConstantFactory.me().getUserEmailByPosition(assignee);
        if (email != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            // 发送人的邮箱
            message.setFrom(from);
            //标题
            message.setSubject("OA审批通知");
            //发给谁  对方邮箱
            message.setTo(email);
            //内容
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(date);
            message.setText("单号-" + reqno + ' ' + usrname +"于" + time + "提交的" + ProcessNameType.valueOf(type) + "待您审批, 地址" + oa_url);
            //发送
            try {
                mailSender.send(message);
            } catch (MailException e) {
                logger.error("邮件发送失败, error is:" + e);
                throw new ServiceException(BizExceptionEnum.MAIL_SEND_ERROR);
            }
        } else {
            logger.error("邮件发送地址不存在，请检查用户表中是否配置了邮件地址");
            throw new ServiceException(BizExceptionEnum.MAIL_SEND_TARGET_ERROR);
        }
    }
}
