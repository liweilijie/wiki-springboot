package com.liwei.wiki.service;

import com.liwei.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    @Async // SpringBoot 另外启动一个线程来做这里面的事情, Async 被调用的地方不能在同一个类里面，一定要切记。还有事务也是这样。
    public void sendInfo(String message, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(message);
    }
}
