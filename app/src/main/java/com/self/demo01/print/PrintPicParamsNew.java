package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class PrintPicParamsNew {
    @SerializedName("type")
    public  int type = 4;
    @SerializedName("command")
    public  String COMMAND = "print_picture";
    public String data;
    public int align;
}
