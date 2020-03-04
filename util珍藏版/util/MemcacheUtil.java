package com.cakes.frameworks.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.danga.MemCached.MemCachedClient;

public class MemcacheUtil {
	public static MemCachedClient getMemCachedClient() {
		return (MemCachedClient) SpringContextUtil.getBean("memcachedClient");
	}

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				new String[] { "file:///E://java_workspace//workspace_8.5//qqDrug//WebRoot//WEB-INF/spring-servlet.xml" });
		MemCachedClient m = (MemCachedClient) SpringContextUtil
				.getBean("memcachedClient");
		m.set("name", "yunhui");
		System.out.println(m.get("name"));
	}
}
