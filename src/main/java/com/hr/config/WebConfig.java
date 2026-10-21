package com.hr.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            String uploadDir = System.getProperty("user.home") + "/employee-images/";

            // ✅ Map /img/** to BOTH static folder AND external folder
            registry.addResourceHandler("/img/**")
                    .addResourceLocations(
                            "file:" + uploadDir,                        // ✅ External uploaded images
                            "classpath:/static/img/"                    // ✅ Static folder images
                    );
        }
    }