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
public class LoginRequredInterceptor implements HandlerInterceptor {
	
	@Autowired
	HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		
		if(hostHolder.getUser()==null){
			response.sendRedirect("/reglogin?next="+request.getRequestURI());
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
