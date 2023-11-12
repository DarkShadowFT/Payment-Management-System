package com.example.pms.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ResponseUtils {

    private ResponseUtils() {

    }

    public static HttpHeaders createLocationHeader(String resourceId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String basePath = request.getServletPath();

        String locationPath = basePath + "/" + resourceId;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(locationPath));
        return headers;
    }

    public static OffsetDateTime convertToOffsetDateTime(LocalDateTime inputDateTime) {
        return OffsetDateTime.of(inputDateTime, ZoneOffset.UTC);
    }
}
