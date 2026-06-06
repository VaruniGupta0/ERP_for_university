package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public Course findById(int courseId) {//  finding by course ID
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Course findByCode(String courseCode) { //finding by course code
        String sql = "SELECT * FROM courses WHERE code = ?";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM courses ORDER BY code";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getERPConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println();
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public List<Course> findAllActive() {

        return findAll();
    }

    public List<Course> findByStatus(String status) {

        return findAll();
    }

    public List<Course> searchCourses(String searchTerm) {
        String sql = "SELECT * FROM courses WHERE (title LIKE ? OR code LIKE ?) ORDER BY code";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public boolean save(Course course) { //saving course
        if (course.getCourseId() > 0) {
            return update(course);
        } else {
            return insert(course);
        }
    }

    private boolean insert(Course course) { // inserting course in ERP
        String sql = "INSERT INTO courses (code, title, credits) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    course.setCourseId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update(Course course) { // update course anything
        String sql = "UPDATE courses SET code = ?, title = ?, credits = ? WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getCourseId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int courseId) { //deleting course
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean codeExists(String code) {
        return codeExists(code, 0);
    }

    public boolean codeExists(String code, int excludeCourseId) {
        String sql = "SELECT COUNT(*) FROM courses WHERE code = ? AND course_id != ?";

        try (Connection conn = DatabaseConnection.getERPConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            stmt.setInt(2, excludeCourseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("code"),
                rs.getString("title"),
                rs.getInt("credits")
        );



        return course;
    }


    public boolean activateCourse(int courseId) {
        return true;
    }


    public boolean deactivateCourse(int courseId) {
        return true;
    }



}