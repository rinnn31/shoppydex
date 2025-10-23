package com.github.rinnn31.shoppydex.utils;

import org.springframework.core.io.ClassPathResource;

import jakarta.servlet.http.HttpServletResponse;

public class WebViewRenderer {
    public static void renderView(HttpServletResponse response, String viewPath) {
        ClassPathResource resource = new ClassPathResource(viewPath);
        try (var inputStream = resource.getInputStream();
             var outputStream = response.getOutputStream()) {
                response.setContentType("text/html;charset=UTF-8");
                inputStream.transferTo(outputStream);
        } catch (Exception e) {
            /* ignored */
        }
    }
}
