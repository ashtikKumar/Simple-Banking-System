import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DisplayServlet")
public class DisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num = Integer.parseInt(request.getParameter("num"));
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, "system","venom");
			String query = "select * from account where num=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, num);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()){
	            out.println("<h2>Account Number: "+rs.getInt(1)+"</h2>");
	            out.println("<h2>Name: "+rs.getString(2)+"</h2>");
	            out.println("<h2>Balance: "+rs.getDouble(3)+"</h2>");
				out.println("Back to <a href='Success.html'>Home</a>");
	        } else {
	            System.out.println("Account not found");
	        }
		}catch(Exception e) {
			out.println("<h2>Exception:"+e.getMessage()+"</h2>");
		}
	}

}
