package com.lovec.mobilesafe.been;

/**
 * Created by lovec on 2016/8/13.
 */
public class BlacknumInfo {
    private String blacknum;
    private int mode;

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    public BlacknumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    public BlacknumInfo() {

    }

    @Override
    public String toString() {
        return "BlacknumInfo{" +
                "blacknum='" + blacknum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
