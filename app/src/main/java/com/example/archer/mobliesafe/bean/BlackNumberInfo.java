package com.example.archer.mobliesafe.bean;

/**
 * Created by Archer on 2016/7/23.
 * <p>
 * 描述:
 * <p>
 * 作者
 */
public class BlackNumberInfo {

    /**
     * 黑名单电话号码
     *
     * 黑名单拦截模式
     * 1.全部拦截，电话拦截和短信拦截
     * 2.电话拦截
     * 3.短信拦截
     */
    private String number;

    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
