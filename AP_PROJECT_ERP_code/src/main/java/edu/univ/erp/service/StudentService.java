package edu.univ.erp.service;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Enrollment;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Grade;
import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.data.EnrollmentDAO;
import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.data.GradeDAO;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class StudentService {
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final SectionDAO sectionDAO;
    private final GradeDAO gradeDAO;

    public StudentService() {
        this.courseDAO = new CourseDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.sectionDAO = new SectionDAO();
        this.gradeDAO = new GradeDAO();
    }

    public List<Course> getCourseCatalog() {
        return courseDAO.findAllActive();
    }


    public List<Section> getSectionsByCourse(String courseCode) {
        try {

            Course course = courseDAO.findByCode(courseCode);
            if (course == null) {
                return new ArrayList<>();
            }


            return sectionDAO.findByCourse(course.getCourseId());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving sections for course: " + courseCode, e);
        }
    }

    public boolean registerForSection(String studentId, String courseCode, String sectionId) {
        try {

            int studentIdInt = Integer.parseInt(studentId);
            int sectionIdInt = Integer.parseInt(sectionId);


            return registerForSection(studentIdInt, sectionIdInt);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid ID format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }


    public List<Enrollment> getStudentEnrollments(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }


    public boolean dropSection(int enrollmentId, int studentId) {
        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        if (enrollment == null || enrollment.getStudentId() != studentId) {
            return false;
        }

        boolean dropped = enrollmentDAO.dropEnrollment(enrollmentId);
        if (dropped) {
            sectionDAO.decrementEnrolledCount(enrollment.getSectionId());
        }
        return dropped;
    }


    public boolean registerForSection(int studentId, int sectionId) {
        if (enrollmentDAO.hasActiveEnrollment(studentId, sectionId)) {
            return false;
        }

        Section section = sectionDAO.findById(sectionId);
        if (section == null || !section.hasAvailableSeats()) {
            return false;
        }

        boolean created = enrollmentDAO.createEnrollment(studentId, sectionId);
        if (created) {
            sectionDAO.incrementEnrolledCount(sectionId);
        }
        return created;
    }


    public List<Grade> getGradesForEnrollment(int enrollmentId) {
        return gradeDAO.findByEnrollmentId(enrollmentId);
    }

    public List<Grade> getStudentGrades(int studentId) {
        List<Enrollment> enrollments = getStudentEnrollments(studentId);
        List<Grade> allGrades = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            allGrades.addAll(getGradesForEnrollment(enrollment.getEnrollmentId()));
        }

        return allGrades;
    }

    public Map<String, Object> calculateGPA(int studentId) {
        List<Enrollment> enrollments = getStudentEnrollments(studentId);
        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if ("COMPLETED".equalsIgnoreCase(enrollment.getStatus())) {

            }
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("gpa", gpa);
        result.put("totalCredits", totalCredits);
        result.put("totalGradePoints", totalGradePoints);

        return result;
    }

    public List<Section> getAvailableSections() {
        return sectionDAO.findAvailableSections();
    }

    public List<String[]> getStudentRegistrationDetails(int studentId) {
        return enrollmentDAO.findEnrollmentDetailsByStudentId(studentId);
    }

}