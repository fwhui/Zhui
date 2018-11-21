package com.hui;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hui.dao.QuestionDao;
import com.hui.model.EntityType;
import com.hui.model.Question;
import com.hui.model.User;
import com.hui.service.FollowService;
import com.hui.service.SensitiveService;
import com.hui.service.UserService;
import com.hui.utils.JedisAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZhuiApplication.class)
//@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserService userService;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveUtil;

    @Autowired
    FollowService followService;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i+1));
            user.setPassword("123");
            user.setSalt("");
            userService.register(user.getName(),user.getPassword());

            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDao.addQuestion(question);
        }
    }
}
