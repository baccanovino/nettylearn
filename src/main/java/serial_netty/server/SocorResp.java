package serial_netty.server;

import java.io.Serializable;

/**
 * Created by Orion on 2018/7/25.
 */
public class SocorResp implements Serializable{
    private static final long serialVersionUID=1L;
    private int chinese;
    private int math;
    private int english;
    private String pj;

    public int getChinese() {
        return chinese;
    }

    public void setChinese(int chinese) {
        this.chinese = chinese;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
    }

    @Override
    public String toString() {
        return "socorresp{" +
                "chinese=" + chinese +
                ", math=" + math +
                ", english=" + english +
                ", pj='" + pj + '\'' +
                '}';
    }
}
