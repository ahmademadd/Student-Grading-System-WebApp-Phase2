package Controllers.Servlets;
import Models.Enrollment;
import Controllers.DAO.MySQLDAO;
import Controllers.DAO.StudentDAO;
import Models.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class Dashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }
        // Retrieve student information
        String studentEmail = (String) session.getAttribute("email");
        StudentDAO dao = new StudentDAO();
        Student student = dao.getStudentByEmail(studentEmail);

        MySQLDAO mysqldao = new MySQLDAO();
        List<Enrollment> list = mysqldao.getEnrollments(studentEmail);

        // Store student data in request scope
        req.setAttribute("studentName", student.getName());
        req.setAttribute("studentEmail", student.getEmail());
        req.setAttribute("grades", list);

        // Forward to dashboard.jsp
        req.getRequestDispatcher("/WEB-INF/Views/dashboard.jsp").forward(req, resp);
    }
}
