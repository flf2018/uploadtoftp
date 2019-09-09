package com.ktxl;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
@SpringBootApplication
public class FilelistenerApplication {
    private  static  Logger log = Logger.getLogger(FilelistenerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(FilelistenerApplication.class, args);
        System.out.println("*********************************启动成功*********************************");
        // 监控目录
        Properties pro = FileListener.getExpProperties();
        String rootDir1 = pro.getProperty("ftp.localfile");
        String rootDir2 = pro.getProperty("ftp.localfile1");
        String[] arrfile=new String[2];
        arrfile[0]=rootDir1;
        arrfile[1]=rootDir2;
        for (int i=0;i<arrfile.length;i++) {
            // 轮询间隔 5 秒
            String rootDir=arrfile[i];
            long interval = TimeUnit.SECONDS.toMillis(1);
            // 创建过滤器
            IOFileFilter directories = FileFilterUtils.and(
                    FileFilterUtils.directoryFileFilter(),
                    HiddenFileFilter.VISIBLE);
            IOFileFilter files = FileFilterUtils.and(
                    FileFilterUtils.fileFileFilter(),
                    FileFilterUtils.suffixFileFilter(".EXP"));
            IOFileFilter filter = FileFilterUtils.or(directories, files);
            // 使用过滤器
            FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
            //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
            observer.addListener(new FileListener());
            //创建文件变化监听器
            FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
            // 开始监控
            try {
                monitor.start();
            } catch (Exception e) {
                log.error("异常处理", e);
            }
        }
    }
}


