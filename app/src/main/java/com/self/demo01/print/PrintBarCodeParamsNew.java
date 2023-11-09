package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class PrintBarCodeParamsNew {
    @SerializedName("type")
    public int type = 2;
    @SerializedName("command")
    public String COMMAND;
    public String code;
    public int align = 0;
    public int width = 2;
    public int height = 162;
    public int hri = 0;
    public String symbology = "CODE128";
}
