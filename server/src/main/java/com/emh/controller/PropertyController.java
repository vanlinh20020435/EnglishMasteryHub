package com.emh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController
{
    public static String JWT_SECRET;
    public static Long JWT_EXPIRATION_MS;

    @Value("${app.jwtSecret}")
    public void setJwtSecret(String jwtSecret){
        PropertyController.JWT_SECRET = jwtSecret;
    }

    @Value("${app.jwtExpirationMs}")
    public void setJwtExpirationMs(String jwtExpirationMs){
        PropertyController.JWT_EXPIRATION_MS = Long.parseLong(jwtExpirationMs);
    }
}