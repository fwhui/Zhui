package com.hui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.hui.dao.QuestionDao;
import com.hui.model.Question;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;
    

    public Question getById(int id) {
        return questionDao.getById(id);
    }

    public int addQuestion(Question question) {
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        // 敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return questionDao.updateCommentCount(id, count);
    }
}
