import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RemoveServlet")
@SuppressWarnings("serial")
public class RemoveServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java1";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            String snoStr = request.getParameter("sno");

            if (snoStr == null || snoStr.isEmpty()) {
                out.println("<p style='color:red;'>Error: Student ID (sno) is missing!</p>");
                return;
            }

            int studentId = Integer.parseInt(snoStr);

            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

                // Delete the student
                String deleteQuery = "DELETE FROM students WHERE sno = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                    stmt.setInt(1, studentId);
                    stmt.executeUpdate();
                }

                // Reset sno values
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("SET @count = 0");
                    stmt.executeUpdate("UPDATE students SET sno = @count := @count + 1");
                }

                response.sendRedirect("StudentsServlet");

            } catch (SQLException e) {
                out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            }
        }
    }
}
