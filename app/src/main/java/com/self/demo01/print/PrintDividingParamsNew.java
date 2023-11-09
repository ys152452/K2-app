package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class PrintDividingParamsNew {
    @SerializedName("type")
    public  int type = 5;
    @SerializedName("command")
    public  String COMMAND = "print_dividing";
    public int style;
    public int height;
}
