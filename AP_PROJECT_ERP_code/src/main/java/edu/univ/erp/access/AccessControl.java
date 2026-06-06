package edu.univ.erp.access;


import edu.univ.erp.domain.User;
import edu.univ.erp.service.MaintenanceService;

public class AccessControl {
    private static MaintenanceService maintenanceService = new MaintenanceService();  // maintenance mode object

    public static boolean canModifyData(User user) {
        // If maintenance mode is ON, only admins can modify data
        if (isMaintenanceMode() && !"ADMIN".equals(user.getRole())) {
            return false;
        }
        return true;
    }
// access controls in different roles
    //first is admin,where it's role implies in functionality
    public static boolean canAccessUserManagement(User user) {
        return "ADMIN".equals(user.getRole());
    }

    public static boolean canAccessCourseManagement(User user) {
        return "ADMIN".equals(user.getRole());
    }

    public static boolean canAccessSectionManagement(User user) {
        return "ADMIN".equals(user.getRole());
    }

    public static boolean canAccessMaintenanceMode(User user) {
        return "ADMIN".equals(user.getRole());
    }

    public static boolean canGradeSection(User instructor, int sectionInstructorId) {
        return "INSTRUCTOR".equals(instructor.getRole()) &&
                instructor.getUserId() == sectionInstructorId;
    }

    public static boolean canViewStudentData(User user, int studentUserId) { // student data is seen by user and instructor
        switch (user.getRole()) {
            case "ADMIN":
                return true;
            case "INSTRUCTOR":

                return true;
            case "STUDENT":
                return user.getUserId() == studentUserId;
            default:
                return false;
        }
    }

    public static boolean isMaintenanceMode() {
        return maintenanceService.isMaintenanceMode();
    } // maintenance mode  functionality

    public static String getAccessDeniedMessage(User user) { // acccess permissions in different roles like student and users are not allowed
        if (isMaintenanceMode() && !"ADMIN".equals(user.getRole())) {
            return "System is in maintenance mode. Changes are not allowed.";
        }
        return "You do not have permission to perform this action.";
    }
}
