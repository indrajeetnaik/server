
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <center>
            <table>
                <tr><td>Group Name</td><td>Username</td><td>Access</td><td>Start Time</td><td>End Time</td><td>Master Key</td><td>Category</td><td>Pseudo Random</td></tr>
                <%
                ServletContext cntxt=getServletContext();
                ArrayList<revocation.RevocationParameters> rlist=(ArrayList<revocation.RevocationParameters>)cntxt.getAttribute("revocation_list");
                if(rlist!=null)
                {
                Iterator<revocation.RevocationParameters> itr=rlist.iterator();
                while(itr.hasNext())
                    {
                    revocation.RevocationParameters rev=itr.next();
                %>
                <tr><td><%=rev.group_name%></td><td><%=rev.username%></td><td><%=rev.access%></td><td><%=rev.start_time%></td><td><%=rev.end_time%></td><td><%=rev.master_key%></td><td><%=rev.category%></td><td><%=rev.srandom%></td></tr>
                <%}
                }%>
            </table>
        </center>
    </body>
</html>
