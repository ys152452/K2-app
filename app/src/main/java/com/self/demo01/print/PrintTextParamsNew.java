package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class PrintTextParamsNew {
    @SerializedName("type")
    public  int type = 1;
    @SerializedName("command")
    public  String COMMAND = "print_text";
    public String text;
    public int align = 0;
    public int size = 24;
    public int bold = 0;
    public int underline = 0;
    public int anticolor = 0;
    public int textspace = 0;
    public int hor_ratio = 1;
    public int ver_ratio = 1;
}
