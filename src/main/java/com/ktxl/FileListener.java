package com.ktxl;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.Properties;

@Component
public class FileListener extends FileAlterationListenerAdaptor  {
    private Logger log = Logger.getLogger(FileListener.class);

    public static Properties getExpProperties(){
        InputStream inputStream = FileListener.class.getClassLoader().getResourceAsStream("application.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            //读取配置文件出错
            e.printStackTrace();
        }
        return trimSpace(p);
    }
    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        Properties pro =  getExpProperties();
        log.info("[新建]:" + file.getAbsolutePath());
        try {
            FileInputStream in=new FileInputStream(file);
            String url = pro.getProperty("ftp.url");
            String username = pro.getProperty("ftp.username");
            String password = pro.getProperty("ftp.password");
            int port = Integer.parseInt(pro.getProperty("ftp.port"));
            String  path=file.getAbsolutePath();
            String[] paths=path.split("\\\\");
            String  basepath="/";
            String filepath = paths[paths.length-2]; //取文件上一级目录
            String fileName=file.getName();
            uploadToServer.uploadFile(url,port,username,password, basepath,filepath,fileName, in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {

        log.info("[修改]:" + file.getAbsolutePath());
        BufferedReader buf = null;
        try {
            buf = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            BufferedReader br = new BufferedReader(buf);
            String line = null;
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 这里释放系统 IO 资源
            try {if (buf != null){buf.close();}}catch (Exception e){}
        }
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        log.info("[删除]:" + file.getAbsolutePath());
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        log.info("[新建]:" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        log.info("[修改]:" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        log.info("[删除]:" + directory.getAbsolutePath());
    }

    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStart(observer);
    }

    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStop(observer);
    }
    /**
     * * 读取properties文件的值中会可能会有空格，需要处理一下。*/
    public static Properties trimSpace(Properties prop) {
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            entry.setValue(entry.getValue().toString().trim());
        }
        return prop;
    }
}

