package com.hui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hui.ZhuiApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZhuiApplication.class)
@WebAppConfiguration
public class ZhuiApplicationTests {

	@Test
	public void contextLoads() {
	}

}
