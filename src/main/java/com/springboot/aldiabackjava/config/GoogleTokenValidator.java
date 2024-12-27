package com.springboot.aldiabackjava.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GoogleTokenValidator  {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static Map<String, Object> getUserDataFromToken(String idTokenString) {
        try {
            GoogleIdToken token = GoogleIdToken.parse(JSON_FACTORY, idTokenString);
            GoogleIdToken.Payload payload = token.getPayload();
            Map<String, Object> userData = new HashMap<>();

            userData.put("userId", payload.getSubject()); // ID del usuario
            userData.put("email", payload.getEmail()); // Correo electrónico
            userData.put("name", payload.get("name")); // Nombre completo
            userData.put("givenName", payload.get("given_name")); // Nombre
            userData.put("familyName", payload.get("family_name")); // Apellido
            userData.put("picture",payload.get("picture"));
            System.out.println(payload);
            return userData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}