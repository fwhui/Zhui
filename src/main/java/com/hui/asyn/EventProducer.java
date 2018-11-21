package com.hui.asyn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hui.utils.JedisAdapter;
import com.hui.utils.RedisKeyUtil;

@Service
public class EventProducer {
	@Autowired
	JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel eventModel){
		try{
			String key=RedisKeyUtil.getEventQueueKey();
			String json=JSONObject.toJSONString(eventModel);
			jedisAdapter.lpush(key,json);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
