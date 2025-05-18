package com.crepestrips.orderservice.command;

public interface OrderCommand {
    void execute();
    void undo();
}