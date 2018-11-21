package com.hui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hui.asyn.EventModel;
import com.hui.asyn.EventProducer;
import com.hui.asyn.EventType;
import com.hui.model.Comment;
import com.hui.model.EntityType;
import com.hui.model.HostHolder;
import com.hui.model.Question;
import com.hui.model.User;
import com.hui.model.ViewObject;
import com.hui.service.CommentService;
import com.hui.service.FollowService;
import com.hui.service.LikeService;
import com.hui.service.QuestionService;
import com.hui.service.UserService;
import com.hui.utils.ZhuiUtil;

@Controller
public class QuestionController {
	private Logger logger=LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	QuestionService questionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
    FollowService followService;
	
    @Autowired
    LikeService likeService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
    EventProducer eventProducer;
	
	@RequestMapping(value="/question/add",method=RequestMethod.POST)
	@ResponseBody
	public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
		try{
			Question question=new Question();
			question.setTitle(title);
			question.setContent(content);
			question.setCommentCount(0);
			question.setCreatedDate(new Date());
			if(hostHolder==null){
				return ZhuiUtil.getJsonString(999);
			}else{
				question.setUserId(hostHolder.getUser().getId());
			}
			if(questionService.addQuestion(question)>0){
				
				 eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
	                        .setActorId(question.getUserId()).setEntityId(question.getId())
	                .setExt("title", question.getTitle()).setExt("content", question.getContent()));
				
				return ZhuiUtil.getJsonString(0);
			}
			
		}catch(Exception e){
			logger.error("提问失败"+e.getMessage());
		}
		return ZhuiUtil.getJsonString(1, "提问失败");
	}
	
	@RequestMapping(value="/question/{qid}")
	public String selectQuestionById(@PathVariable("qid")int qid,Model model){
		Question question = questionService.getById(qid);
        model.addAttribute("question", question);
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if(hostHolder.getUser()==null){
            	vo.set("liked", 0);
            }
            else{
            	vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments", vos);
        
        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }
        
		return "detail";
	}

}
