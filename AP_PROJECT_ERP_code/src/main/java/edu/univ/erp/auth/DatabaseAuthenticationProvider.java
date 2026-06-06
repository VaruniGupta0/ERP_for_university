package edu.univ.erp.auth;
import edu.univ.erp.domain.User;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.auth.PasswordEncoder;
import edu.univ.erp.util.Logger;

public class DatabaseAuthenticationProvider {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private Logger logger;

    public DatabaseAuthenticationProvider() {
        this.userDAO = new UserDAO();
        this.passwordEncoder = new PasswordEncoder();
        this.logger = Logger.getInstance();
    }

    public User authenticate(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) {
                logger.warn("Authentication failed: User not found - " + username);
                return null;
            }

            if (!"ACTIVE".equals(user.getStatus())) {
                logger.warn("Authentication failed: User account is " + user.getStatus() + " - " + username);
                return null;
            }

            if (userDAO.authenticate(username, password)) {
                logger.info("Database authentication successful for user: " + username);
                return user;
            } else {
                logger.warn("Database authentication failed: Invalid password for user - " + username);
            }
        } catch (Exception e) {
            logger.error("Database authentication error for user: " + username, e);
        }

        return null;
    }

    public boolean supports(Class<?> authentication) {
        return true;
    }

    public User authenticateWithSecurity(String username, String password, String clientInfo) {
        User user = authenticate(username, password);

        if (user != null) {
            logger.info("User authenticated from: " + clientInfo + " - " + username);

            updateLastLoginInfo(user.getUserId(), clientInfo);
        }

        return user;
    }

    private void updateLastLoginInfo(int userId, String clientInfo) {

        logger.debug("Updated last login info for user ID: " + userId + " from: " + clientInfo);
    }
}