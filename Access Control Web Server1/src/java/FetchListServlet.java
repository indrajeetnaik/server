

import connection.MysqlConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchListServlet extends HttpServlet {
   
   Connection con;
   PreparedStatement pst;
   ResultSet rst;

   boolean allowed=false;
   String category;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try 
        {
            allowed=false;
            String group=request.getParameter("group_name");
            String username=request.getParameter("username");
            String branch=request.getParameter("branch");

             ServletContext cntxt=getServletContext();
             ArrayList<revocation.RevocationParameters> rlist=(ArrayList<revocation.RevocationParameters>)cntxt.getAttribute("revocation_list");
                if(rlist!=null)
                {
                Iterator<revocation.RevocationParameters> itr=rlist.iterator();
                while(itr.hasNext())
                    {
                    revocation.RevocationParameters rev=itr.next();
                    String user1=rev.username;
                    String group1=rev.group_name;
                   
                    if(group.equals(group1)&&username.equals(user1))
                    {
                        allowed=true;
                        category=rev.category;
                       
                        
                    }
                
                }
                }

           
            if(allowed)
            {
            con=MysqlConnection.getMysqlConnection();
            pst=con.prepareStatement("select * from data_list where group_name=? and categry=? and branch=?");
            pst.setString(1, group);
            pst.setString(2,category);
            pst.setString(3,branch);
            rst=pst.executeQuery();
            String data=null;
            int i=0;
            while(rst.next())
            {
                if(i==0)
                {
                    i=1;
                    data=rst.getString("group_name")+"^"+rst.getString("username")+"^"+rst.getString("file_name");
                }
                else
                {
                    data=data+"~"+rst.getString("group_name")+"^"+rst.getString("username")+"^"+rst.getString("file_name");
                }
            }


            out.println(data);
            rst.close();
            pst.close();
            con.close();
            }
            else
            {
                 out.println("No File Available");
            }
           
        }
        catch(Exception e)
        {
             out.println(e.getLocalizedMessage());
        }
        finally {
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
