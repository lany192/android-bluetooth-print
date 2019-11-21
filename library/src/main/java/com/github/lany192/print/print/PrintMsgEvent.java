package com.github.lany192.print.print;

/**
 *
 * print message event
 */
public class PrintMsgEvent {
    public int type;
    public String msg;

    public PrintMsgEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
