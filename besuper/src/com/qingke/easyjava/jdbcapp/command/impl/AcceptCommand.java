package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.PlayerCommand;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class AcceptCommand extends PlayerCommand {

    @Override
    public Object execute(Player playerProfile) {

        long qid = QingkeConsole.askUserNumber("Question ID: ");
        long aid = QingkeConsole.askUserNumber("Answer ID: ");

        Question myQuestion = null;
        for (Question question : playerProfile.getQuestions()) {
            if (question.getId() == qid) {
                myQuestion = question;
                break;
            }
        }

        if (myQuestion == null) {
            QingkeConsole.println("The question(ID: " + qid + ") is not yours! You can't determine the best answers");
            return null;
        }

        int score = myQuestion.getCredit();
        score = (score <= 0) ? 1 : score;

        Answer bestAnswer = null;
        for (Answer answer : myQuestion.getAnswers()) {
            if (answer.getId() == aid) {
                bestAnswer = answer;
                break;
            }
        }

        if (bestAnswer == null) {
            QingkeConsole.println("Answer(ID: " + aid
                    + " is not exist or not belong to the question.");
            return null;
        }

        Player player = bestAnswer.getFrom();
        player.setScore(player.getScore() + score);

        myQuestion.setBestAnswer(bestAnswer);
        bestAnswer.setBestAnswer(true);

        BeSuperDao dao = new BeSuperDao();
        dao.acceptAnswer(myQuestion, bestAnswer);
        dao.updatePlayer(player);
        return null;
    }

}
