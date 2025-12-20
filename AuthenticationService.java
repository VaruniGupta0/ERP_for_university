package edu.univ.erp.auth;

import edu.univ.erp.domain.User;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.util.Logger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuthenticationService {
    private final UserDAO userDAO;
    private final Logger logger;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_MINUTES = 15;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
        this.logger = Logger.getInstance();
    }

    public User authenticate(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            
            if (user == null) {
                logger.warn("Login attempt with non-existent username: " + username);
                return null;
            }

            if (isUserLockedOut(user)) {
                logger.warn("Login attempt for locked out user: " + username);
                return null;
            }

            if (user.getPassword().equals(password)) {

                resetFailedAttempts(user);
                logger.info("User authenticated successfully: " + username);
                return user;
            } else {

                incrementFailedAttempts(user);
                logger.warn("Invalid credentials for user: " + username);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error authenticating user: " + username, e);
            return null;
        }
    }

    private boolean isUserLockedOut(User user) {
        if (user.getLockedUntil() == null) {
            return false; // Not locked
        }
        
        Timestamp lockedUntil = user.getLockedUntil();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lockTime = lockedUntil.toLocalDateTime();
        
        if (now.isBefore(lockTime)) {
            return true; // Still locked
        } else {

            resetFailedAttempts(user);
            return false;
        }
    }


    private void incrementFailedAttempts(User user) {
        int currentAttempts = (user.getFailedAttempts() == null ? 0 : user.getFailedAttempts());
        currentAttempts++;
        
        user.setFailedAttempts(currentAttempts);
        
        if (currentAttempts >= MAX_FAILED_ATTEMPTS) {
            // Lock the account
            LocalDateTime lockUntil = LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES);
            user.setLockedUntil(Timestamp.valueOf(lockUntil));
            logger.warn("Account locked for user: " + user.getUsername() + 
                       " (too many failed attempts). Locked until: " + lockUntil);
        }
        
        userDAO.save(user);
    }


    private void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        userDAO.save(user);
    }


    public int getLockoutTimeRemaining(User user) {
        if (user.getLockedUntil() == null) {
            return 0;
        }
        
        LocalDateTime lockUntil = user.getLockedUntil().toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(lockUntil)) {
            return (int) java.time.temporal.ChronoUnit.SECONDS.between(now, lockUntil);
        }
        
        return 0;
    }

    public boolean isValidSession(Integer userId) {
        try {
            User user = userDAO.findById(userId);
            boolean valid = (user != null);
            logger.debug("Session check for userId " + userId + ": " + valid);
            return valid;
        } catch (Exception e) {
            logger.error("Error checking session validity for userId: " + userId, e);
            return false;
        }
    }

    public User getUserById(Integer userId) {
        try {
            return userDAO.findById(userId);
        } catch (Exception e) {
            logger.error("Error getting user by ID: " + userId, e);
            return null;
        }
    }
}
