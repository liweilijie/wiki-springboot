package com.liwei.wiki.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

//@ComponentScan({"com.liwei", "com.test"}) // 扩大扫描的路径,增加多个包路径
@ComponentScan("com.liwei") // 扩大扫描的路径
@SpringBootApplication
@MapperScan("com.liwei.wiki.mapper") // 让mybatis 扫描mapper
public class WikiApplication {
	private static final Logger log = LoggerFactory.getLogger(WikiApplication.class);
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WikiApplication.class);
		Environment env = app.run(args).getEnvironment();
		log.info("启动成功！！");
		log.info("地址：\thttp://127.0.0.1:{}", env.getProperty("server.port"));
	}

}
