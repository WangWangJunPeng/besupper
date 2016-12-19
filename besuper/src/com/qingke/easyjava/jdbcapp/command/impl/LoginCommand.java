package com.qingke.easyjava.jdbcapp.command.impl;

import java.util.List;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.Command;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.PlayerCredential;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class LoginCommand implements Command {

    @Override
    public Object execute() {
        Player player = null;
        BeSuperDao dao = new BeSuperDao();

        // ask input
        String username = QingkeConsole.askUserInput("Username: ");
        String password = QingkeConsole.askUserInput("Password: ");

        PlayerCredential credential = new PlayerCredential(username, password);
        player = dao.login(credential);

        if (player != null) {
            List<Question> myQuestions = dao.getQuestionsFrom(player);
            List<Answer> myAnswers = dao.getAnswersFrom(player);

            player.setQuestions(myQuestions);
            player.setAnswers(myAnswers);

            QingkeConsole.println("Welcome back, " + player.getName() + "! Type 'help' for command usage.");
        } else {
            QingkeConsole.println("Username/Password is not valid!");
        }
        return player;
    }
}
