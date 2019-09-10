package com.ktxl;

import com.ktxl.util.FtpConfig;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
@SpringBootApplication
public class FilelistenerApplication {
    private  static  Logger log = Logger.getLogger(FilelistenerApplication.class);
    public static void main(String[] args) {
        ApplicationContext cxt= SpringApplication.run(FilelistenerApplication.class, args);
        FtpConfig ftpConfig= (FtpConfig) cxt.getBean("ftpConfig");
        System.out.println("*********************************启动成功*********************************");
        System.out.println("url："+ftpConfig.getUrl());
        System.out.println("port："+ftpConfig.getPort());
        System.out.println("name："+ftpConfig.getUsername());
        System.out.println("目录一："+ftpConfig.getLocalfile());
        System.out.println("目录二："+ftpConfig.getLocalfile1());
        System.out.println("pwd："+ftpConfig.getPassword());
        System.out.println("*********************************配置信息*********************************");
        // 监控目录
        String rootDir1 =ftpConfig.getLocalfile();
        String rootDir2 = ftpConfig.getLocalfile1();
        String[] arrfile=new String[2];
        arrfile[0]=rootDir1;
        arrfile[1]=rootDir2;
        for (int i=0;i<arrfile.length;i++) {
            // 轮询间隔 5 秒
            String rootDir=arrfile[i];
            FileListener.listenerExp(rootDir);

        }
    }
}


