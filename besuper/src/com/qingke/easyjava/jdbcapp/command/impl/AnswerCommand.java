package com.qingke.easyjava.jdbcapp.command.impl;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.PlayerCommand;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class AnswerCommand extends PlayerCommand {

    @Override
    public Object execute(Player playerProfile) {

        int qid = QingkeConsole.askUserNumber("Question ID: ");

        BeSuperDao dao = new BeSuperDao();
        Question question = dao.getQuestion(qid);

        if (question == null) {
            QingkeConsole.println("Invalid question id! id: " + qid);
            return null;
        }
        
        if (!question.isOpen()) {
            QingkeConsole.println("The question is resolved. Try another one.");
            return null;
        }

        if (playerProfile.getQuestions().contains(question)) {
            QingkeConsole.println("You are not allowed to answer the quetion from yourself!");
            return null;
        }

        String value = QingkeConsole.askUserInput("Enter your answer: ");
        Answer answer = new Answer(-1, value, playerProfile);
        answer.setQuestionId(qid);

        dao.upsertAnwser(answer);
        return null;
    }

}
