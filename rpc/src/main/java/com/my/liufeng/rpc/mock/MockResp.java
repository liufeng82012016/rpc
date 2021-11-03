package com.my.liufeng.rpc.mock;

public class MockResp {

    private String str;
    private Long len;
    private Integer count;
    private Boolean succ;

    public MockResp() {
        this.str = "hello";
        this.len = 133L;
        this.count = -3;
        this.succ = true;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Long getLen() {
        return len;
    }

    public void setLen(Long len) {
        this.len = len;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getSucc() {
        return succ;
    }

    public void setSucc(Boolean succ) {
        this.succ = succ;
    }

    @Override
    public String toString() {
        return "MockResp{" +
                "str='" + str + '\'' +
                ", len=" + len +
                ", count=" + count +
                ", succ=" + succ +
                '}';
    }
}
