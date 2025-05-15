package com.crepestrips.adminservice.command;


import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class AdminInvoker {
    private final Stack<AdminCommand> commandHistory = new Stack<>();

    public void executeCommand(AdminCommand command) {
        command.execute();
        commandHistory.push(command);
    }


}
