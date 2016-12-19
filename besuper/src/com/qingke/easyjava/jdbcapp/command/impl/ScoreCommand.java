package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.command.PlayerCommand;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class ScoreCommand extends PlayerCommand {

    @Override
    public Object execute(Player playerProfile) {

        QingkeConsole.println("=============== Player: " + playerProfile.getName() + " ===============");
        QingkeConsole.println("Total asked: " + playerProfile.getQuestions().size() + " time(s)");
        QingkeConsole.println("Total answered: " + playerProfile.getAnswers().size() + " time(s)");

        int bestCount = 0;
        for (Answer answer : playerProfile.getAnswers()) {
            if (answer.isBestAnswer()) {
                bestCount++;
            }
        }

        QingkeConsole.println("Best Answers: " + bestCount + " time(s)");
        QingkeConsole.println("Final Score: " + playerProfile.getScore());

        String level = "N/A";
        if (playerProfile.getScore() < 30) {
            level = "Loser";
        } else if (playerProfile.getScore() < 60) {
            level = "Fair";
        } else if (playerProfile.getScore() < 80) {
            level = "Good Student";
        } else if (playerProfile.getScore() < 100) {
            level = "Superb!";
        } else {
            level = "DAMN CRAZY!!!";
        }

        QingkeConsole.println("Your current Level£º " + level);

        return null;
    }

}
