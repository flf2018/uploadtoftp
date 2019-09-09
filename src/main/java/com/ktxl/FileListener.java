package com.ktxl;

import com.ktxl.util.FtpConfig;
import com.ktxl.util.SpringUtil;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.TimeUnit;


public class FileListener extends FileAlterationListenerAdaptor  {
    private Logger log = Logger.getLogger(FileListener.class);
    private static FtpConfig ftpConfig;
    static {
        ftpConfig=(FtpConfig)SpringUtil.getBean("ftpConfig",FtpConfig.class);
    }
    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        log.info("[新建]:" + file.getAbsolutePath());
        try {
            FileInputStream in=new FileInputStream(file);
            String url = ftpConfig.getUrl();
            String username = ftpConfig.getUsername();
            String password =ftpConfig.getPassword();
            int port = ftpConfig.getPort();
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
     * 文件监听
     * @param rootDir
     */
    public static void listenerExp(String rootDir){
        // 轮询间隔 1 秒
        long interval = TimeUnit.SECONDS.toMillis(1);
        // 创建过滤器
        try {
            IOFileFilter directories = FileFilterUtils.and(
                    FileFilterUtils.directoryFileFilter(),
                    HiddenFileFilter.VISIBLE);
            IOFileFilter files  = FileFilterUtils.and(
                    FileFilterUtils.fileFileFilter(),
                    FileFilterUtils.suffixFileFilter(".EXP"));
            IOFileFilter filter = FileFilterUtils.or(directories, files);
            // 使用过滤器
            FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
            //不使用过滤器
            //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
            observer.addListener(new FileListener());
            //创建文件变化监听器
            FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
            // 开始监控
            monitor.start();
            //monitor.stop();
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}

