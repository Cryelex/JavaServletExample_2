package webtest.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webtest.model.Department;
import webtest.service.DbService;


@WebServlet("/AddFaculty")
public class AddFaculty extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public AddFaculty()
    {
        super();
    }

    protected void doGet( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
    	DbService dbService = new DbService();
        request.setAttribute( "departments", dbService.getDepartments() );
        dbService.close();
        request.getRequestDispatcher( "/WEB-INF/AddFaculty.jsp" )
            .forward( request, response );
    }

    protected void doPost( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
        String departmentName = request.getParameter( "department" );
        String faculty =  request.getParameter( "faculty" );
        Boolean tab=false;
        if( request.getParameter( "chair" ) != null ) {
        	System.out.println("tab");
        	tab= true; 
        }

        DbService dbService = new DbService();
        Department temp = dbService.getDepartment(departmentName);
        dbService.addFaculty( faculty, temp.getId(),tab);
        dbService.close();

        response.sendRedirect( "DisplayFaculty" );
    }

}
