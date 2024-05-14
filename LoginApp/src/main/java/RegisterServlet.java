

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		try {
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");
			String cnfm = request.getParameter("cnfm");
			
			if(pwd.equals(cnfm)) {
				String driver = "oracle.jdbc.driver.OracleDriver";
				String url = "jdbc:oracle:thin:@localhost:1521:xe";
				
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url,"system","venom");
				String query = "insert into register values(?,?)";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, uname);
				ps.setString(2, pwd);
				
				int count = ps.executeUpdate();
				if(count>0) {
					out.println("<h4 style='color:red'> Registration Done - Login Now</h4>");
					RequestDispatcher rd = request.getRequestDispatcher("Login.html");
					rd.include(request, response);
				}else {
					throw new InputMismatchException("Registration Failed - Try Again");
				}
				
			}else {
				
				throw new InputMismatchException("Password Mismatch");
			}
			
		} catch(InputMismatchException e) {
			out.println("<h4 style='color:red'>"+e.getMessage()+"</h4>");
			RequestDispatcher rd = request.getRequestDispatcher("Register.html");
			rd.include(request, response);
		} catch(Exception e) {
			out.println("<h4 style='color:red'>Error:"+e.getMessage()+"</h4>");
			RequestDispatcher rd = request.getRequestDispatcher("Register.html");
			rd.include(request, response);
		}

	}

}
