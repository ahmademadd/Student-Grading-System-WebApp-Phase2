package Models;
import Controllers.DAO.DatabaseConnection;
import Controllers.DAO.MySQLDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Enrollment {
    private final int enrollment_id;
    private final int student_id;
    private final int course_id;
    private final Double grade;
    private final Connection con = DatabaseConnection.getConnection();
    MySQLDAO dao = new MySQLDAO();
    private final Course course;
    private final Student student;

    public Enrollment(int enrollment_id, int student_id, int course_id, Double grade) {
        this.enrollment_id = enrollment_id;
        this.student_id = student_id;
        this.course_id = course_id;
        this.grade = grade;
        course = dao.getCourse(course_id);
        student = dao.getStudent(student_id);
    }

    public Double getGrade() {
        return grade;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getEnrollment_id() {
        return enrollment_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public Double getCourseAvg() {
        String query = "SELECT AVG(grade) AS avg_grade FROM enrollments WHERE course_id = ?";
        double avgGrade = 0.0;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, getCourse_id());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    avgGrade = rs.getDouble("avg_grade");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avgGrade;
    }

    public int getStudentRank() {
        String query = "SELECT COUNT(*) + 1 AS `rank` " +
                "FROM enrollments e1 " +
                "WHERE e1.course_id = ? " +
                "AND e1.grade > (" +
                "    SELECT e2.grade" +
                "    FROM enrollments e2" +
                "    WHERE e2.student_id = ?" +
                "    AND e2.course_id = ?" +
                "    LIMIT 1" +
                ")";

        int rank = -1; // Default value if not found

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, getCourse_id());
            stmt.setInt(2, getStudent_id());
            stmt.setInt(3, getCourse_id());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    rank = rs.getInt("rank");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // More detailed error output
        }
        return rank;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }
}