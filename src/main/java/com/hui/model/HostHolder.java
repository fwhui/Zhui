package com.hui.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
	private static ThreadLocal<User> users=new ThreadLocal<>();
	
	public void set(User user){
		users.set(user);
	}
	
	public User getUser(){
		return users.get();
	}
	
	public void clear(){
		users.remove();
	}
}
