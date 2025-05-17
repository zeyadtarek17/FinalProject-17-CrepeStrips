package com.crepestrips.adminservice.command;


import org.springframework.stereotype.Service;

import java.util.Stack;
@Service
public class AdminInvoker {
    private AdminCommand command;

    public void setCommand(AdminCommand command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }
}

