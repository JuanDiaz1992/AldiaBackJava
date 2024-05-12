package com.springboot.aldiabackjava.utils;
import org.mindrot.jbcrypt.BCrypt;

public class EncryptPassword {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean validatePassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }
}
