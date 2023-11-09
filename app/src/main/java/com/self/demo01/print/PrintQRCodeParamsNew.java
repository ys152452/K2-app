package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class PrintQRCodeParamsNew {
    @SerializedName("type")
    public   int type = 3;
    @SerializedName("command")
    public  String COMMAND = "print_qrcode";
    public String code;
    public int align = 0;
    public int size = 4;
    public int el = 0;
}
