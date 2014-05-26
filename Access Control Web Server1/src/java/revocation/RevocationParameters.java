
package revocation;


public class RevocationParameters
{
    public String group_name;
    public String username;
    public String start_time;
    public String end_time;
    public String access;
    public String master_key;
    public String category;
    public String srandom;

    public RevocationParameters(String gname,String user,String stime,String etime,String access,String master_key,String category,String srandom)
    {
        this.group_name=gname;
        this.username=user;
        this.start_time=stime;
        this.end_time=etime;
        this.access=access;
        this.master_key=master_key;
        this.category=category;
        this.srandom=srandom;

    }

}
