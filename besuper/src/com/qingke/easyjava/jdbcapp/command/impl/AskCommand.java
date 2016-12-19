package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.PlayerCommand;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class AskCommand extends PlayerCommand {

    @Override
    public Object execute(Player playerProfile) {

        String value = QingkeConsole.askUserInput("Your qusetion: ");
        int credit = QingkeConsole.askUserNumberWithDefault("Credit [optional, Integer only]: ", 0);
        if (credit > playerProfile.getScore()) {
            QingkeConsole.println("Insufficient credit! Reset credits to your remains: " + playerProfile.getScore());
            credit = playerProfile.getScore();
        }

        Question question = new Question(-1, value, playerProfile, credit);
        playerProfile.getQuestions().add(question);
        playerProfile.setScore(playerProfile.getScore() - credit);

        BeSuperDao dao = new BeSuperDao();
        dao.upsertQuestion(question);
        dao.updatePlayer(playerProfile);
        return null;
    }

}
