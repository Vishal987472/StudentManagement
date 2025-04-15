import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Statement;

@WebServlet("/AddServlet")
@SuppressWarnings("serial")
public class AddServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java1";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Add Student</title>");
            
            // CSS Styling
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: linear-gradient(90deg, #C7C5F4, #776BCC); text-align: center; color: white; }");
            out.println(".container { width: 50%; margin: 20px auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); color: black; }");
            out.println("h2 { color: #776BCC; }");
            out.println("form { margin: 20px auto; text-align: left; max-width: 400px; }");
            out.println("label { display: block; margin: 10px 0 5px; font-weight: bold; }");
            out.println("input { width: 100%; padding: 8px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 5px; }");
            out.println("button { padding: 10px 30px; border: none; background: #776BCC; color: white; font-weight: 600; font-size: 16px; cursor: pointer; border-radius: 5px; margin-top: 10px; }");
            out.println("button:hover { background: #5a4bb7; }");
            out.println("</style>");
            
            out.println("</head><body>");
            out.println("<div class='container'>");
            out.println("<h2>Add New Student</h2>");
            out.println("<form action='AddServlet' method='POST'>");
            out.println("<label>UID:</label> <input type='text' name='uid' required>");
            out.println("<label>Name:</label> <input type='text' name='name' required>");
            out.println("<label>Enrolled Date:</label> <input type='text' name='enrolled' required>");
            out.println("<label>Course:</label> <input type='text' name='course' required>");
            out.println("<label>Address:</label> <input type='text' name='address' required>");
            out.println("<button type='submit'>Add Student</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uid = request.getParameter("uid");
        String name = request.getParameter("name");
        String enrolled = request.getParameter("enrolled");
        String course = request.getParameter("course");
        String address = request.getParameter("address");

        String insertQuery = "INSERT INTO students (uid, name, enrolled, course, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, uid);
            stmt.setString(2, name);
            stmt.setString(3, enrolled);
            stmt.setString(4, course);
            stmt.setString(5, address);

            int rowsInserted = stmt.executeUpdate();
            response.setContentType("text/html");

            if (rowsInserted > 0) {
                // Reset sno values after inserting
                try (Statement stmt2 = conn.createStatement()) {
                    stmt2.executeUpdate("SET @count = 0");
                    stmt2.executeUpdate("UPDATE students SET sno = @count := @count + 1");
                }
                response.sendRedirect("StudentsServlet");
            } else {
                response.sendRedirect("StudentsServlet?error=Failed to add student");
            }

        } catch (SQLException e) {
            response.setContentType("text/html");
            try (PrintWriter out = response.getWriter()) {
                out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            }
        }
    }
}
