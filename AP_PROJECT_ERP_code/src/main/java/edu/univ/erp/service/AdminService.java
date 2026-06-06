package edu.univ.erp.service;

import edu.univ.erp.domain.User;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Student;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.data.StudentDAO;
import edu.univ.erp.data.InstructorDAO;
import edu.univ.erp.data.SettingsDAO;


import edu.univ.erp.domain.Settings;


import java.util.List;
import java.util.List;

public class AdminService {
    private UserDAO userDAO;
    private CourseDAO courseDAO;
    private SectionDAO sectionDAO;
    private StudentDAO studentDAO;
    private InstructorDAO instructorDAO;
    private SettingsDAO settingsDAO;

    public AdminService() {
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.sectionDAO = new SectionDAO();
        this.studentDAO = new StudentDAO();
        this.instructorDAO = new InstructorDAO();
        this.settingsDAO = new SettingsDAO();
    }


    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean createUser(User user) {
        return userDAO.save(user);
    }

    public boolean updateUser(User user) {
        return userDAO.save(user);
    }

    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }


    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public boolean createCourse(Course course) {
        return courseDAO.save(course);
    }

    public boolean updateCourse(Course course) {
        return courseDAO.save(course);
    }

    public boolean deleteCourse(int courseId) {
        return courseDAO.delete(courseId);
    }

    public boolean activateCourse(int courseId) {
        return courseDAO.activateCourse(courseId);
    }

    public boolean deactivateCourse(int courseId) {
        return courseDAO.deactivateCourse(courseId);
    }

    public boolean updateCourseStatus(int courseId, String newStatus) {
        if (!List.of("ACTIVE", "INACTIVE", "ARCHIVED").contains(newStatus.toUpperCase())) {
            System.err.println("Invalid course status: " + newStatus);
            return false;
        }

        if (newStatus.equalsIgnoreCase("ACTIVE")) {
            return activateCourse(courseId);
        } else if (newStatus.equalsIgnoreCase("INACTIVE")) {
            return deactivateCourse(courseId);
        } else {
            Course course = courseDAO.findById(courseId);
            if (course == null) return false;
            course.setStatus(newStatus.toUpperCase());
            return courseDAO.save(course);
        }
    }

    public boolean courseCodeExists(String code) {
        return courseDAO.codeExists(code);
    }

    public List<Section> getAllSections() {
        return sectionDAO.findAllActive();
    }

    public boolean createSection(Section section) {
        return sectionDAO.save(section);
    }

    public boolean updateSection(Section section) {
        return sectionDAO.save(section);
    }

    public boolean deleteSection(int sectionId) {
        return sectionDAO.delete(sectionId);
    }


    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }


    public List<Instructor> getAllInstructors() {
        return instructorDAO.findAll();
    }

    public boolean setMaintenanceMode(boolean mode) {
        return settingsDAO.updateValue("maintenance_mode", mode ? "true" : "false");
    }

    public boolean isMaintenanceMode() {
        Settings setting = settingsDAO.findByKey("maintenance_mode");
        return setting != null && "true".equalsIgnoreCase(setting.getValue());
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }


    public Course getCourseById(int courseId) {
        return courseDAO.findById(courseId);
    }

    public Section getSectionById(int sectionId) {
        return sectionDAO.findById(sectionId);
    }

    public List<User> getAllInstructorsAsUsers() {
        List<User> allUsers = userDAO.findAll();
        return allUsers.stream()
                .filter(user -> "INSTRUCTOR".equalsIgnoreCase(user.getRole()))
                .toList();
    }
}