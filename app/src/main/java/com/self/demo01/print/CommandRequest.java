package com.self.demo01.print;

import com.google.gson.annotations.SerializedName;

public class CommandRequest {
    /**
     * "data":{
     * "serviceId":"SunmiInnerPrinter",
     * "command":"printerInit",
     * "params":null
     * }
     */
    @SerializedName("command")
    public String command;
    @SerializedName("params")
    public Object params;

    @Override
    public String toString() {
        return "CommandRequest{" +
                "command='" + command + '\'' +
                ", params=" + params +
                '}';
    }
}
