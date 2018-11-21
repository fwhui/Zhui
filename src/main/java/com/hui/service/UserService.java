package com.hui.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hui.dao.LoginTicketDao;
import com.hui.dao.UserDao;
import com.hui.model.LoginTicket;
import com.hui.model.User;
import com.hui.utils.ZhuiUtil;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginTicketDao loginTicketDao;

	public Map<String,String> register(String username,String password){
		Map<String,String> map=new HashMap<>();
		if(StringUtils.isBlank(username)){
			map.put("msg", "用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msg", "密码不能为空");
			return map;
		}
		
		User user=userDao.selectByName(username);
		
		if(user!=null){
			map.put("msg", "该昵称已存在，换一个昵称试试");
			return map;
		}
		user=new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().substring(0, 6));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setPassword(ZhuiUtil.MD5(password+user.getSalt()));
		
		userDao.addUser(user);
		
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		
		return map;
	}
	
	public User getUser(int id) {
		return userDao.selectById(id);
	}
	
	public User selectByName(String name){
		return userDao.selectByName(name);
	}

	public Map<String, String> login(String username, String password) {
		Map<String,String> map=new HashMap<>();
		if(StringUtils.isBlank(username)){
			map.put("msg", "用户名不能为空");
			return map;
		}
		if(StringUtils.isBlank(password)){
			map.put("msg", "密码不能为空");
			return map;
		}
		
		User user=userDao.selectByName(username);
		
		if(user==null){
			map.put("msg", "该用户不存在，是不是忘了昵称呢");
			return map;
		}
		
		if(!ZhuiUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
			map.put("msg", "密码错误哦，忘了的话可以点击忘记密码哦");
			return map;
		}
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		map.put("userId", user.getId()+"");
		return map;
	}
	
	public String addLoginTicket(int userId){
		LoginTicket loginTicket=new LoginTicket();
		loginTicket.setUserId(userId);
		Date now=new Date();
		now.setTime(360000*24*7+now.getTime());
		loginTicket.setExpired(now);
		loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicket.setStatus(0);
		loginTicketDao.addTicket(loginTicket);
		return loginTicket.getTicket();
	}

	public void logout(String ticket) {
		loginTicketDao.updateStatus(ticket, 1);
	}

}
