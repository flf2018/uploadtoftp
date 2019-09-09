package com.ktxl.util;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component
public class FtpConfig {

    @Value(value = "${ftp.url}") public  String  url;
    @Value(value = "${ftp.port}") public  int port;
    @Value(value = "${ftp.username}") public  String username;
    @Value(value = "${ftp.password}") public  String password;
    @Value(value = "${ftp.localfile}") public  String localfile;
    @Value(value = "${ftp.localfile1}") public String localfile1;

    public String getLocalfile() {
        return localfile;
    }

    public void setLocalfile(String localfile) {
        this.localfile = localfile;
    }

    public String getLocalfile1() {
        return localfile1;
    }

    public void setLocalfile1(String localfile1) {
        this.localfile1 = localfile1;
    }
   public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
