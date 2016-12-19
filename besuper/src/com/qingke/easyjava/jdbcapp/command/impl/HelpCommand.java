package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.command.Command;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class HelpCommand implements Command {

    @Override
    public Object execute() {
        QingkeConsole.println("======== I want to be XUE BA commands ========");
        QingkeConsole.println("= EXIT   - Exit the application");
        QingkeConsole.println("= HELP   - Print command usage");
        QingkeConsole.println("= LOGIN  - Login the application");
        QingkeConsole.println("= SIGNUP - Sign up an player account");
        QingkeConsole.println("= LIST   - List questions for ALL|MINE|OPEN|ID mode");
        QingkeConsole.println("= ASK    - Ask a question");
        QingkeConsole.println("= ANSWER - Answer a question");
        QingkeConsole.println("= ACCEPT - Set best answer for a question");
        QingkeConsole.println("= SCORE  - Show player's score");

        return null;
    }
}
