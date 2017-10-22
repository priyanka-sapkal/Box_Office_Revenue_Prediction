package com.Servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Model.Processing_Starts;



@WebServlet("/Processing")
public class Processing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public Processing() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title= request.getParameter("title");
		
		Processing_Starts ps = new Processing_Starts();
		try {
			ps.starts(title);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

}
