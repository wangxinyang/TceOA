package com.tce.oa;

import com.tce.oa.core.common.constant.state.ProcessAssigneeName;
import com.tce.oa.core.common.handler.MailSendHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TceOaApplicationTests {

    @Autowired
    private MailSendHandler mailSendHandler;

    @Test
    public void test01() {
        String reqno = "111111111111111";
        Integer type = 1;
        String usrname = "wxy";
        Date date = new Date();
        mailSendHandler.sendTaskMail(ProcessAssigneeName.ASSIGNEE_ASSISANT, reqno, usrname, type, date);
    }

}
