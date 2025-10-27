package com.github.rinnn31.shoppydex.utils;

import java.io.IOException;
import java.util.Map;

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

    public static void renderViewWithAttribute(HttpServletResponse response, String viewPath, Map<String, Object> attributes) {
        try {
            String renderedContent = renderViewToString(viewPath, attributes);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(renderedContent);
        } catch (IOException e) {
            /* ignored */
        }
    }

    public static String renderViewToString(String viewPath, Map<String, Object> attributes) {
        ClassPathResource resource = new ClassPathResource(viewPath);
        try (var inputStream = resource.getInputStream()) {
            String content = new String(inputStream.readAllBytes(), "UTF-8");
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                content = content.replace(placeholder, entry.getValue().toString());
            }
            return content;
        } catch (Exception e) {
            return "";
        }
    }
}
