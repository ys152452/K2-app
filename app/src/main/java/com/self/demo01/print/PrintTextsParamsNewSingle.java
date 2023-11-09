package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrintTextsParamsNewSingle {
    @SerializedName("type")
    public int type = 6;
    @SerializedName("command")
    public String COMMAND = "print_texts";
    public List<PrintTextsItemParamsNew> columns;
}
