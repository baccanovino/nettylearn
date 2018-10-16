package serial_netty.server;

import java.io.Serializable;

/**
 * Created by Orion on 2018/7/25.
 */
public class SocorReq implements Serializable{
    private static final long serialVersionUID=1L;
    private String studentname;
    private String lience;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getLience() {
        return lience;
    }

    public void setLience(String lience) {
        this.lience = lience;
    }

    @Override
    public String toString() {
        return "socorreq{" +
                "studentname='" + studentname + '\'' +
                ", lience='" + lience + '\'' +
                '}';
    }
}
