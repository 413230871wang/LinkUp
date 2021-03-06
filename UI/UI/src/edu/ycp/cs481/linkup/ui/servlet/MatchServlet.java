package edu.ycp.cs481.linkup.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs481.linkup.controller.IndexController;
import edu.ycp.cs481.linkup.controller.MatchingController;
import edu.ycp.cs481.linkup.model.Matching;
import edu.ycp.cs481.linkup.model.Path;
import edu.ycp.cs481.linkup.persistence.PersistenceException;
import edu.ycp.cs481.linkup.persistence.SQLconnection;


public class MatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String DB_USERNAME = "ajcummins";
	private static String DB_PASSWORD = "root";
	
	int user_id;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		//Get userid from the url passed
		Path urlPath = new Path(req.getPathInfo());
		System.out.println("Path = " + req.getPathInfo());
		user_id = Integer.parseInt(urlPath.getUserIDFromPath());
		
		MatchingController controller = new MatchingController();
		String[][] match = null;
		try {
			match = controller.Matching(user_id);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String table = "";
		if(match == null){
			req.setAttribute("match", "No Matches at this time. :(");
		}
		else{
			for(int i = 0; i < match[1].length; i++){
				table = table + "<tr><td>" + (i+1) + ") " + match[0][i] + "      </td><td>"+ (match[1][i]) +" % match </td><td><input name='submit' value='View "+ match[0][i] +" Profile' type='submit'  style='width:200px'/></td></tr>";
			}
			req.setAttribute("match", table);
		}
		req.getRequestDispatcher("/_view/userMatch.jsp").forward(req, resp);
	}
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String buttonAction = req.getParameter("submit");
		if(buttonAction.equals("Profile")){
			System.out.print("\nthis is the user id:" + user_id);
			resp.sendRedirect("userProfile/"+ user_id);					
		}if(buttonAction.equals("Messages")){ //go to the user's messages page
			System.out.print("\nthis is the user id:" + user_id);
			resp.sendRedirect("matchMessages/"+ user_id);
			
		}if(buttonAction.equals("Update Looking For")){ //go to update looking for info
			resp.sendRedirect("UpdatelookingFor/"+ user_id);
			
		}if(buttonAction.equals("Edit Profile Info")){
			
			resp.sendRedirect("EditProInfo/"+ user_id);
		
		}if(buttonAction.equals("Logout")){
			
			// Reset Session values
			HttpSession thisSession = req.getSession(true);
			thisSession.setAttribute("loggedInUser", null);
			thisSession.setAttribute("login.isDone", false);
			thisSession.setAttribute("usersLookingFor",null);
			thisSession.setAttribute("usersProfile",null);
						
			resp.sendRedirect("index");
		
		}else{
			System.out.print("\nthis is the value:" + buttonAction);
			MatchingController controller = new MatchingController();
			int id = 0;
			try {
				id = controller.MatchID(buttonAction);
				resp.sendRedirect("MatchProfile/"+ user_id + "/" + id);
			} catch (PersistenceException e) {
				e.printStackTrace();
			}				
		}
	}	
	
}

