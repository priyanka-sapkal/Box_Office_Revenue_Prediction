package com.Servlet;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Model.Initializer;

import static com.Model.Initializer.inConn1;


@WebServlet("/InsertMovie")
public class InsertMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public InsertMovie() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Initializer in = new Initializer();
		
		String SQL_INSERT = "INSERT INTO movie(TITLE,HASHTAG,SEQUEL,GENRE,ACTOR,ACTRESS,URL,HOLIDAY,ActorName,Actressname) VALUES(?,?,?,?,?,?,?,?,?,?)";
	    PreparedStatement statement;
		try {
			in.connection_open();
			statement = inConn1.prepareStatement(SQL_INSERT);
			statement.setString(1, request.getParameter("title")); 
			statement.setString(2, request.getParameter("hashtag"));
			statement.setInt(3, Integer.parseInt(request.getParameter("sequel")));
			statement.setDouble(4, Double.parseDouble(request.getParameter("genre")));
			statement.setDouble(5, Double.parseDouble(request.getParameter("actorfc")));
			statement.setDouble(6, Double.parseDouble(request.getParameter("actressfc")));
			statement.setString(7, request.getParameter("url"));
			statement.setString(8, request.getParameter("holiday"));
			statement.setString(9, request.getParameter("actor"));
			statement.setString(10, request.getParameter("actress"));
			statement.executeUpdate();
			
			in.connection_close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		
		ServletContext sc = this.getServletContext();
		request.setAttribute("movie", request.getParameter("title"));
		RequestDispatcher rd = sc.getRequestDispatcher("/Jsp/insert.jsp");
		rd.include(request, response);
		
		
	}

}
