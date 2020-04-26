package com.example.demo;

import com.example.demo.server.NettyClient;
import com.example.demo.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        try {
            System.out.println("准备启动客户端");
            new NettyClient("wss://www.bitmex.com/realtime").start();
            System.out.println("客户端启动完成");

            System.out.println("准备启动服务端");
            new NettyServer(12345).start();
            System.out.println("服务端启动完成");
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }

}
