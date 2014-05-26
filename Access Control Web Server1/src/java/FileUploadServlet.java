
import connection.MysqlConnection;
import java.io.File;
import java.util.Date;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import revocation.RevocationParameters;
public class FileUploadServlet extends HttpServlet {

   String filePath;
   File uploadedFile;

   Connection con;
   PreparedStatement pst;
   ResultSet rst;

   String group_name,username,srandom,filename,category,branch;
   String user;
   String access,master_key;
   SecretKey key;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
          response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        group_name=request.getParameter("group_name");
        user=request.getParameter("user");
        boolean allow=false;

        Date date = new Date();
    SimpleDateFormat simpDate;

    simpDate = new SimpleDateFormat("kk:mm:ss");
    simpDate.format(date);


         int hour=date.getHours();
         int min=date.getMinutes();
System.out.println("user="+user);
         ArrayList<RevocationParameters> rlist=(ArrayList<RevocationParameters>)getServletContext().getAttribute("revocation_list");
         Iterator<RevocationParameters> itr=rlist.iterator();
         while(itr.hasNext())
         {
             RevocationParameters rparam=itr.next();
             System.out.println(rparam.username);
            
            System.out.println(user);
             if(rparam.group_name.equals(group_name)&&rparam.username.equals(username))
             {
                 access=rparam.access;
                 master_key=rparam.master_key;
                 srandom=rparam.srandom;
                 String start_time=rparam.start_time;
                 String end_time=rparam.end_time;

                 StringTokenizer st=new StringTokenizer(start_time,":");
                 int shour=Integer.parseInt(st.nextToken());
                 int smin=Integer.parseInt(st.nextToken());

                 st=new StringTokenizer(end_time,":");
                 int ehour=Integer.parseInt(st.nextToken());
                 int emin=Integer.parseInt(st.nextToken());

                 if(shour<hour)
                 {
                     if(ehour>hour)
                     {
                        allow=true;
                        break;
                     }
                     else if(ehour==hour)
                     {
                         if(emin>min)
                         {
                             allow=true;
                              break;
                         }
                         else
                         {
                             allow=false;
                              break;
                         }

                     }

                 }
                 else if(shour==hour)
                 {
                     if(smin<min)
                     {
                         allow=true;
                          break;
                     }
                     else
                     {
                         allow=false;
                          break;
                     }
                 }


             }

         }
         if(allow)
         {
        try
        {
         filePath=getServletContext().getRealPath("files");


         FileItemFactory factory = new DiskFileItemFactory();
         ServletFileUpload upload = new ServletFileUpload(factory);
         List uploadedItems = null;
         FileItem fileItem = null;


         uploadedItems = upload.parseRequest(request);
         Iterator i = uploadedItems.iterator();
         while (i.hasNext())
         {
          fileItem = (FileItem) i.next();
          if (fileItem.isFormField() == false)
          {
          if (fileItem.getSize() > 0)
          {
            File uploadedFile = null;
            String myFullFileName = fileItem.getName(), myFileName = "", slashType =
            (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
            int startIndex = myFullFileName.lastIndexOf(slashType);
            myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
            File f=new File(filePath+"/"+group_name+"/"+username);
            if(!f.exists())
            {
                f.mkdirs();
            }
             filename=myFileName;
            uploadedFile = new File(filePath+"/"+group_name+"/"+username, myFileName);

            fileItem.write(uploadedFile);

          }
        }
        else
        {
              if(fileItem.getFieldName().equals("group_name"))
            {
              group_name=fileItem.getString();
            }
               if(fileItem.getFieldName().equals("username"))
            {
              username=fileItem.getString();
            }
               if(fileItem.getFieldName().equals("category"))
            {
              category=fileItem.getString();
            }
                if(fileItem.getFieldName().equals("branch"))
            {
              branch=fileItem.getString();
            }



        }
      }


            StringTokenizer st=new StringTokenizer(category,",");
            while(st.hasMoreTokens())
            {
            con=MysqlConnection.getMysqlConnection();
            pst=con.prepareStatement("insert into data_list (group_name,usernamename,file_name,categry,branch) values (?,?,?,?,?)");

            pst.setString(1,group_name);
            pst.setString(2,username);
            pst.setString(3,filename);
            pst.setString(4,st.nextToken());
            pst.setString(5,branch);

            pst.executeUpdate();
            pst.close();
            con.close();
            }
            out.println("Data Uploaded Successfully");


        }

        catch(Exception e)
        {
            out.println("Message"+e.getLocalizedMessage());
        }
        finally
        {
            out.close();
        }
         }
         else
        {
             out.println("Access Denied");
        }
    }

     public byte[] decryptdata(byte[] data,SecretKey key)
    {
        try
        {

         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] decrypted = cipher.doFinal(data);
         return decrypted;
        }
        catch(Exception e)
        {
            System.out.print("Error:"+e.getLocalizedMessage());
        }
        return null;

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
