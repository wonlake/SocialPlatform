package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by meijun on 2016/11/18.
 */
@ConfigurationProperties(prefix="SrPlatform")
public class SrPlatformSettings {
    private String uploadPath;
    private int maxImageSize;

    public void setUploadPath(String path) {
        this.uploadPath = path;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public int getMaxImageSize() {
        return maxImageSize;
    }
}
