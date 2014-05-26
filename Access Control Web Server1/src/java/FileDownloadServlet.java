
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import revocation.RevocationParameters;
import sun.misc.BASE64Encoder;

public class FileDownloadServlet extends HttpServlet {
   
     String access;
     String master_key;
     String username;
     SecretKey key;
     String srandom;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        try
        {
        String group_name=request.getParameter("group_name");
        username=request.getParameter("user");
        String from_user=request.getParameter("from_user");
        
        boolean allow=false;

        Date date = new Date();
    SimpleDateFormat simpDate;

    simpDate = new SimpleDateFormat("kk:mm:ss");
    simpDate.format(date);


         int hour=date.getHours();
         int min=date.getMinutes();


         ArrayList<RevocationParameters> rlist=(ArrayList<RevocationParameters>)getServletContext().getAttribute("revocation_list");
         Iterator<RevocationParameters> itr=rlist.iterator();
         while(itr.hasNext())
         {
             RevocationParameters rparam=itr.next();
             if(rparam.group_name.equals(group_name)&&rparam.username.equals(from_user))
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
         String filename=request.getParameter("filename");
        String fPath=getServletContext().getRealPath("files");
        File file_to_decrypt= new File(fPath+"/"+group_name+"/"+username,filename);
        FileInputStream fis=new FileInputStream(file_to_decrypt);
        byte[] data=new byte[fis.available()];
        fis.read(data);
        fis.close();
        

        key=generateAESKey(access+master_key+srandom+from_user);
        String edata=encryptdata(data, key);

        FileOutputStream fos=new FileOutputStream(new File(fPath+"/"+group_name+"/"+username,"e"+filename));
        fos.write(edata.getBytes());
        fos.close();


        String filePath ="http://localhost:8080/access/files/"+group_name+"/"+username;//getServletContext().getRealPath("files");
       
        System.out.println(filePath);
        // URL url = new
        // URL("http://localhost:8080/Works/images/abt.jpg");
     //   response.setContentType("text/plain");
     //   response.setHeader("Content-Disposition", "attachment; filename="+filename);
        URL url = new URL(filePath+"/"+"e"+filename);
        URLConnection connection = url.openConnection();
        InputStream stream = connection.getInputStream();

        BufferedOutputStream outs = new BufferedOutputStream(out);
        int len;
        byte[] buf = new byte[1024];
        while ((len = stream.read(buf)) > 0) {
            outs.write(buf, 0, len);
        }
        outs.close();
       }
        else
        {
             out.println("Access Denied");
        }


        }
         catch(Exception e)
        {
           out.println("Error:"+e.getLocalizedMessage());
        }
        finally {
            out.close();
        }
    }

      public SecretKeySpec generateAESKey(String param)
    {
         SecretKeySpec secretKeySpec=null;
        try
        {
         byte[] key = param.getBytes("UTF-8");
         MessageDigest sha = MessageDigest.getInstance("SHA-1");
         key = sha.digest(key);
         byte[] key2 = Arrays.copyOfRange(key,key.length-16,key.length);
         key = Arrays.copyOfRange(key,key.length-16,key.length); // use only first 128 bit
         secretKeySpec = new SecretKeySpec(key, "AES");
        }
        catch(Exception e)
        {
           System.out.print("Error:KEy"+e.getLocalizedMessage());
        }
         return secretKeySpec;
    }

       public String encryptdata(byte[] data,SecretKey key)
    {
        try
        {

         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         byte[] encrypted = cipher.doFinal(data);
         String CipherText = new BASE64Encoder().encode(encrypted);
         return CipherText;
        }
        catch(Exception e)
        {
            System.out.println("Error:"+e.getLocalizedMessage());
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
