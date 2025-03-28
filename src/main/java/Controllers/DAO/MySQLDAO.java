package Controllers.DAO;
import Controllers.Observer.GradeNotifier;
import Models.Course;
import Models.Enrollment;
import Models.Student;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDAO {
    private final Connection con;
    private final StudentDAO studentDAO;

    public MySQLDAO() {
        this.con = DatabaseConnection.getConnection();
        this.studentDAO = new StudentDAO();
    }

    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }

    public boolean registerStudent(String name, String email, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO Students (name, email, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean validateLogin(String email, String password) {
        String sql = "SELECT password_hash FROM Students WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                return checkPassword(password, hashedPassword);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Enrollment> getEnrollments(String email) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enrollments WHERE student_id = (SELECT student_id FROM Students WHERE email = ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                enrollments.add(new Enrollment(rs.getInt("enrollment_id"), rs.getInt("student_id"), rs.getInt("course_id"), rs.getDouble("grade")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return enrollments;
    }

    public String getGrades(String email) {
        return studentDAO.getGrades(email);
    }

    public boolean gradeStudent(String email, int courseID, Double grade) {
        boolean isGraded = studentDAO.gradeStudent(email, courseID, grade);
        GradeNotifier.getInstance().notifyObservers(email, getGrades(email));
        return isGraded;
    }

    public Course getCourse(int courseId) {
        String query = "SELECT * FROM Courses WHERE course_id = ?";

        Course course = null;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    course = new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getString("course_code")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return course;
    }

    public Student getStudent(int student_id) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, student_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getInt("student_id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}