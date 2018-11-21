package com.hui.interceptor;


import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hui.dao.LoginTicketDao;
import com.hui.dao.UserDao;
import com.hui.model.HostHolder;
import com.hui.model.LoginTicket;
import com.hui.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor {
	
	@Autowired
	LoginTicketDao loginTicketDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String ticket=null;
		if(request.getCookies()!=null){
			for (Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals("ticket")){
					ticket=cookie.getValue();
					break;
				}
			}
		}
		if(ticket!=null){
			LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
			if(loginTicket==null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
				return true;
			}
			User user = userDao.selectById(loginTicket.getUserId());
			hostHolder.set(user);
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView modelAndView)
			throws Exception {
		if(modelAndView!=null){
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		hostHolder.clear();
	}

}
