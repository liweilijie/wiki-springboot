package com.liwei.wiki.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ComponentScan({"com.liwei", "com.test"}) // 扩大扫描的路径,增加多个包路径
@ComponentScan("com.liwei") // 扩大扫描的路径
@SpringBootApplication
@MapperScan("com.liwei.wiki.mapper") // 让mybatis 扫描mapper
// Schedule 启用定时任务
// 启动定时器,所有定时器都是在一个线程里面做的，所以会错过，错过了就错过了不会再执行了。
// 比较好的可以用 quartz 定时任务框架。
// 在线 cron 表达式里面去生成表达式，不用去记。
@EnableScheduling
@EnableAsync // 异步化，另外启动一个线程来执行异步的内容
public class WikiApplication {
	private static final Logger log = LoggerFactory.getLogger(WikiApplication.class);
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WikiApplication.class);
		Environment env = app.run(args).getEnvironment();
		log.info("启动成功！！");
		log.info("地址：\thttp://127.0.0.1:{}", env.getProperty("server.port"));
	}

}
