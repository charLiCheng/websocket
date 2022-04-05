package com.example.websocket.controller;

import com.example.websocket.controller.websocket.WiselyMessage;
import com.example.websocket.controller.websocket.WiselyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class WSControler {

//    @RequestMapping(value = "/ws")
//    public String index(Model model, ModelMap modelMap) {
//
//        return "ws";
//    }

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public WiselyResponse say(WiselyMessage message) throws InterruptedException {

        Thread.sleep(3000);
        System.out.println("请求过来啦-======");
        return new WiselyResponse("Welcome, "+message.getName()+"!");
    }

    //通过simpMessagingTemplate向浏览器发送消息
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //在spring mvc种, 可以直接在参数中获得principal,principle中包含当前用户的信息
    @MessageMapping("/chat")
    public void handleChat(Principal principal, String msg){

        //这里来了个硬编码,如果发送人事cxl,就发给wisely,反之发给cxl,可以根据实际需求改代码
        if (principal.getName().endsWith("cxl")){
            messagingTemplate.convertAndSendToUser("wisely",
                    //messagingTemplate向用户发送消息,第一个参数事接收者,第二个事浏览器的订阅地址,第三个消息本身
                    "queue/notifications",principal.getName()+"-send发送:"+msg);
        }else {
            messagingTemplate.convertAndSendToUser("cxl",
                    "queue/notifications",principal.getName()+"-send发送: "+msg);
        }


    }


}
