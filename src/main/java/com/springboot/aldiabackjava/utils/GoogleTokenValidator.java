package com.springboot.aldiabackjava.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;


public class GoogleTokenValidator  {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static boolean verifyGoogleToken(String idTokenString) {
        try {
            // Crear el verificador de ID Token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JSON_FACTORY)
                    .setAudience(Arrays.asList("223417773060-r5uvcrb3eavj40qnpkgf3a8ksdp6jkiu.apps.googleusercontent.com"))  // Tu Client ID
                    .build();

            // Parsear el token ID string a un objeto GoogleIdToken
            GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, idTokenString);

            // Verificar el token
            return verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
