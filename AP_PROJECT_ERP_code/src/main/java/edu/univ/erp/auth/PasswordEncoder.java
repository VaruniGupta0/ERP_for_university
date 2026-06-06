package edu.univ.erp.auth;
import edu.univ.erp.util.PasswordUtil;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncoder {

    // Using sal packahe to get Hash password
    public String encode(CharSequence rawPassword) {
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(rawPassword.toString(), salt);
        return salt + ":" + hashedPassword;
    }
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.contains(":")) {
            return false;//password cross-verified
        }

        String[] parts = encodedPassword.split(":", 2);
        String salt = parts[0];
        String storedHash = parts[1];

        return PasswordUtil.verifyPassword(rawPassword.toString(), salt, storedHash);
    }


    public String generateTemporaryPassword() {
        return PasswordUtil.generateTemporaryPassword();// we can have other passwords options
    }

    public boolean isStrongPassword(String password) {
        return PasswordUtil.isStrongPassword(password);
    }// we can choose for strong password


    public int getPasswordStrength(String password) {//returning password strength
        if (password == null) return 0;

        int score = 0;

        // Length check
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;

        // Character variety checks
        if (password.matches(".[A-Z].")) score++;
        if (password.matches(".[a-z].")) score++;
        if (password.matches(".[0-9].")) score++;
        if (password.matches(".[!@#$%^&()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++;

        return Math.min(score, 4);
    }

    //creating password hash
    public String createHash(String password) {
        String salt = PasswordUtil.generateSalt();
        return salt + ":" + PasswordUtil.hashPassword(password, salt);
    }
}