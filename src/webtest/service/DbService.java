package webtest.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import webtest.model.*;

public class DbService {

    private String url = "jdbc:mysql://cs3.calstatela.edu/cs3220stu20";

    private String username = "cs3220stu20";
    private String password = "CeZuqkyts2Ym";

    private Connection connection;

    public DbService()
    {
        try
        {
            connection = DriverManager.getConnection( url, username, password );
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        if( connection != null )
        {
            try
            {
                connection.close();
            }
            catch( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

    public List<Department> getDepartments()
    {
        List<Department> entries = new ArrayList<Department>();

        try
        {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "select * from department" );
            while( rs.next() )
            {
            	Department entry = new Department();
            	entry.setId(rs.getInt( "id" ));
                entry.setName( rs.getString( "name" ) );
                entries.add( entry );
                
                String sql = "select * from faculty where d_id = ?";
                PreparedStatement pstmt = connection.prepareStatement( sql );
                pstmt.setInt( 1, entry.getId() );
                ResultSet rs2 = pstmt.executeQuery();
                while( rs2.next() )
                {
                	Faculty entry2 = new Faculty();
                    entry2.setName( rs2.getString( "name" ) );
                    entry2.setChair(rs2.getBoolean("is_chair"));
                    
                    List<Faculty> temp = entry.getFaculty();
                    temp.add(entry2);
                    entry.setFaculty(temp);
                }
                pstmt.close();
                
            }
            stmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }

        return entries;
    }

    public Department getDepartment( int id )
    {
    	Department entry = new Department();
        try
        {
            String sql = "select * from department where id = ?";
            PreparedStatement pstmt = connection.prepareStatement( sql );
            pstmt.setInt( 1, id );
            ResultSet rs = pstmt.executeQuery();
            if( rs.next() )
            {
            	entry.setId(rs.getInt( "id" ));
                entry.setName( rs.getString( "name" ) );
                
                String sql1 = "select * from faculty where d_id = ?";
                PreparedStatement pstmt1 = connection.prepareStatement( sql1 );
                pstmt1.setInt( 1, id );
                ResultSet rs2 = pstmt1.executeQuery();
                while( rs2.next() )
                {
                	Faculty entry2 = new Faculty();
                    entry2.setName( rs2.getString( "name" ) );
                    entry2.setChair(rs2.getBoolean("is_chair"));
                    List<Faculty> temp = entry.getFaculty();
                    temp.add(entry2);
                    entry.setFaculty(temp);
                }
                pstmt1.close();
                
            }
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        return entry;
    }
    
    public Department getDepartment( String name )
    {
    	Department entry = new Department();
        try
        {
            String sql = "select * from department where name = ?";
            PreparedStatement pstmt = connection.prepareStatement( sql );
            pstmt.setString( 1, name );
            ResultSet rs = pstmt.executeQuery();
            if( rs.next() )
            {
            	entry.setId(rs.getInt( "id" ));
                entry.setName( rs.getString( "name" ) );
                
                String sql1 = "select * from faculty where d_id = ?";
                PreparedStatement pstmt1 = connection.prepareStatement( sql1 );
                pstmt1.setInt( 1, entry.getId() );
                ResultSet rs2 = pstmt1.executeQuery();
                while( rs2.next() )
                {
                	Faculty entry2 = new Faculty();
                    entry2.setName( rs2.getString( "name" ) );
                    entry2.setChair( rs2.getBoolean( "is_chair" ) );
                    
                    List<Faculty> temp = entry.getFaculty();
                    temp.add(entry2);
                    entry.setFaculty(temp);
                }
                pstmt1.close();
            }
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        return entry;
    }

    public int addDepartment( String name)
    {
        int id = 0;
        try
        {
            String sql = "insert into department (name) values (?)";
            PreparedStatement pstmt = connection.prepareStatement( sql,
                Statement.RETURN_GENERATED_KEYS );
            pstmt.setString( 1, name );
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if( rs.next() ) id = rs.getInt( 1 );
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        return id;
    }
    
    public int addFaculty( String name, int id2, boolean chair)
    {
        int id = 0;
        try
        {
            String sql = "insert into faculty (d_id, name, is_chair) values (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement( sql,
                Statement.RETURN_GENERATED_KEYS );
            pstmt.setInt( 1, id2 );
            pstmt.setString( 2, name);
            pstmt.setBoolean( 3, chair);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if( rs.next() ) id = rs.getInt( 1 );
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        return id;
    }

    public void updateDepartment( int id, String name)
    {
        try
        {
            String sql = "update department set name = ?, where id = ?";
            PreparedStatement pstmt = connection.prepareStatement( sql );
            pstmt.setString( 1, name );
            pstmt.setInt( 2, id );
            pstmt.executeUpdate();
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public void deleteDepartment( int id )
    {
        try
        {
            String sql = "delete from department where id = ?";
            PreparedStatement pstmt = connection.prepareStatement( sql );
            pstmt.setInt( 1, id );
            pstmt.executeUpdate();
            pstmt.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }
}

