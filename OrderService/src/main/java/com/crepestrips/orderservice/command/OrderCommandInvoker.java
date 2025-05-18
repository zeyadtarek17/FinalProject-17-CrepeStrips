package com.crepestrips.orderservice.command;

import java.util.Stack;

public class OrderCommandInvoker {
    private final Stack<OrderCommand> commandHistory = new Stack<>();
    
    public void executeCommand(OrderCommand command) {
        command.execute();
        commandHistory.push(command);
    }
    
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            OrderCommand lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }
}