package com.hui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.hui.service.CommentService;
import com.hui.service.LikeService;
import com.hui.utils.ZhuiUtil;

@Controller
public class LikeController {
	@Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;
    
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhuiUtil.getJsonString(999);
        }
        
        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));
        
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhuiUtil.getJsonString(0, String.valueOf(likeCount));
        
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ZhuiUtil.getJsonString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ZhuiUtil.getJsonString(0, String.valueOf(likeCount));
    }
}
