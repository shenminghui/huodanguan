package com.huodan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    
    public static final String UPLOAD_PATH = "uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 创建上传目录
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:" + uploadDir.getAbsolutePath() + "/");
    }
} 