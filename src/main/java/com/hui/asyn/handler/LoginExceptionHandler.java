package com.hui.asyn.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hui.asyn.EventHandler;
import com.hui.asyn.EventModel;
import com.hui.asyn.EventType;
import com.hui.utils.MailSender;

@Component
public class LoginExceptionHandler implements EventHandler {
	@Autowired
	MailSender mailSender; 

	@Override
	public void doHandle(EventModel model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", model.getExt("username"));
		mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆IP异常", "mails/login_exception.html", map);

	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}
}
