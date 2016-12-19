package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.command.Command;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class ExitCommand implements Command {

    @Override
    public Object execute() {
        QingkeConsole.terminate();
        return null;
    }

}
