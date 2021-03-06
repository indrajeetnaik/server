/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import connection.MysqlConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Kitty
 */
public class FileUploadServlet extends HttpServlet {

   String filePath;
   File uploadedFile;

   Connection con;
   PreparedStatement pst;
   ResultSet rst;

   String keyword,filename,group;
   String access,master_key;
   SecretKey key;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
          response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
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
            File f=new File(filePath+"/"+group);
            if(!f.exists())
            {
                f.mkdirs();
            }
             filename=myFileName;
            uploadedFile = new File(filePath+"/"+group, myFileName);

            fileItem.write(uploadedFile);

          }
        }
          else
        {
              if(fileItem.getFieldName().equals("keyword"))
            {
              keyword=fileItem.getString();
            }
               if(fileItem.getFieldName().equals("group"))
            {
              group=fileItem.getString();
            }





        }

      }
         try {
		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
	}
 
	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
	PreparedStatement pst;
	ResultSet rst;

 
	try {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/privacy","root", "root");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
 
     if (connection != null) {
	
	try{
		System.out.println("You made it, take control your database now!");
		pst=connection.prepareStatement("insert into data_list (filename,keyword_list,group_name) values (?,?,?)");
 		pst.setString(1,filename);
        	pst.setString(2,keyword);
            	pst.setString(3,group);

            pst.executeUpdate();
            pst.close();
            con.close();

            out.println("Data Uploaded Successfully");
}
catch (SQLException e) {
                System.out.println("jhhhhhhhhhhhhhhhhhh");
                e.printStackTrace();
                return;
}               
}

        finally
        {
            out.close();
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
