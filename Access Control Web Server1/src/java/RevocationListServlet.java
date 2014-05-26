
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import revocation.RevocationParameters;

public class RevocationListServlet extends HttpServlet {
       
    ArrayList<revocation.RevocationParameters> rlist=new ArrayList<RevocationParameters>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            rlist=new ArrayList<RevocationParameters>();
            String group_name=request.getParameter("group_name");
            String username=request.getParameter("usernames");
            String master_key=request.getParameter("master_key");
            String start_time=request.getParameter("start_times");
            String end_time=request.getParameter("end_times");
            String access=request.getParameter("access");
            String category=request.getParameter("category");
            String srandom=request.getParameter("srandom");

            StringTokenizer st=new StringTokenizer(username,"^");
            String[] usernames=new String[st.countTokens()];
            int k=0;
            while(st.hasMoreTokens())
            {
                usernames[k++]=st.nextToken();
            }

            st=new StringTokenizer(access,"^");
            String[] access_list=new String[st.countTokens()];
            k=0;
            while(st.hasMoreTokens())
            {
                access_list[k++]=st.nextToken();
            }

             st=new StringTokenizer(start_time,"^");
            String[] start_times=new String[st.countTokens()];
            k=0;
            while(st.hasMoreTokens())
            {
                start_times[k++]=st.nextToken();
            }

             st=new StringTokenizer(end_time,"^");
            String[] end_times=new String[st.countTokens()];
            k=0;
            while(st.hasMoreTokens())
            {
                end_times[k++]=st.nextToken();
            }

            st=new StringTokenizer(category,"^");
            String[] cat=new String[st.countTokens()];
            k=0;
            while(st.hasMoreTokens())
            {
                cat[k++]=st.nextToken();
            }

             st=new StringTokenizer(srandom,"^");
            String[] sradom=new String[st.countTokens()];
            k=0;
            while(st.hasMoreTokens())
            {
                sradom[k++]=st.nextToken();
            }

            ServletContext cntxt=getServletContext();
            for(int i=0;i<usernames.length;i++)
            {
               rlist.add(new RevocationParameters(group_name,usernames[i],start_times[i], end_times[i],access_list[i],master_key,cat[i],sradom[i]));
            }

             cntxt.setAttribute("revocation_list", rlist);
           
        }
         catch(Exception e)
        {

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
