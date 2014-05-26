/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import connection.MysqlConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kitty
 */
public class SerachServlet extends HttpServlet {
   
   Connection con;
   PreparedStatement pst;
   ResultSet rst;
   HashMap<String,String> kwrd_file_map=new HashMap<String, String>();
   HashMap<Integer,String> document_map=new HashMap<Integer, String>();
   ArrayList<String> file_list=new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try 
        {
            String sent_keywords=request.getParameter("keyword");
            String group=request.getParameter("group");
            StringTokenizer digits=new StringTokenizer(sent_keywords,",");

            int[] rmatrix=new int[digits.countTokens()];
            int i=0;
            while(digits.hasMoreTokens())
            {

                rmatrix[i++]=Integer.parseInt(digits.nextToken());
               // out.println(rmatrix[i]);
            }

            
            con=MysqlConnection.getMysqlConnection();
            pst=con.prepareStatement("select * from data_list where group_name=?");
            pst.setString(1, group);
            rst=pst.executeQuery();
            ArrayList<String> arr=new ArrayList<String>();
            while(rst.next())
            {
                
                arr.add(rst.getString("keyword_list"));
                kwrd_file_map.put(rst.getString("keyword_list"),rst.getString("filename"));
            }
            pst.close();

            

            int[][] docs=new int[arr.size()][rmatrix.length];
            Iterator<String> itr=arr.iterator();
             int row=0;
            while(itr.hasNext())
            {
                String doc=itr.next();

                int col=0;
                StringTokenizer st=new StringTokenizer(doc,",");
                while(st.hasMoreTokens())
                {
                    docs[row][col++]=Integer.parseInt(st.nextToken());
                }
                document_map.put(row,kwrd_file_map.get(doc));
                row++;
            }

             

            int[][] result=new int[arr.size()][rmatrix.length];
            for(int m=0;m<arr.size();m++)
            {
                for(int n=0;n<rmatrix.length;n++)
                {
                  result[m][n]=rmatrix[n]*docs[m][n];

                  
                }
            }

            
             for(int m=0;m<arr.size();m++)
            {
                for(int n=0;n<rmatrix.length;n++)
                {
                  if(result[m][n]==1)
                  {
                   
                    file_list.add(document_map.get(m));
                    break;
                  }

                }
            }


            

            int count=0;
            String result_data=null;
            Iterator<String> itr5=file_list.iterator();
            while(itr5.hasNext())
            {
                if(count==0)
                {
                    result_data=itr5.next();
                    count=1;
                }
                else
                {
                    result_data=result_data+","+itr5.next();
                }


            }

            con.close();

            arr.clear();
            file_list.clear();
          

            out.println(result_data);
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
