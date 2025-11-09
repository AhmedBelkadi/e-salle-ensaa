package com.esalle.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static void main(String[] args) {
        String pwd = (args != null && args.length > 0) ? args[0] : "password123";
        String hash = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
        System.out.println(hash);
    }
}
