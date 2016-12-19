package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.Command;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.PlayerCredential;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class SignupCommand implements Command {

    @Override
    public Object execute() {
        BeSuperDao dao = new BeSuperDao();

        String username = QingkeConsole.askUserInput("Username: ");
        String password = QingkeConsole.askUserInput("Password: ");
        String name = QingkeConsole.askUserInput("Display Name: ", true);
        if ("".equalsIgnoreCase(name)) {
            name = username;
        }

        PlayerCredential credential = new PlayerCredential(-1, name, 0, username, password);
        Player player = dao.signup(credential);

        return player;
    }

}
