package com.qingke.easyjava.jdbcapp.command.impl;

import java.util.ArrayList;
import java.util.List;

import com.qingke.easyjava.jdbcapp.BeSuperDao;
import com.qingke.easyjava.jdbcapp.command.PlayerCommand;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class ListCommand extends PlayerCommand {

    @Override
    public Object execute(Player playerProfile) {

        BeSuperDao dao = new BeSuperDao();

        String type = QingkeConsole.askUserInput("List questions for (ALL|MINE|OPEN|ID): ");
        if (!"ALL|MINE|OPEN|ID".contains(type.toUpperCase())) {
            QingkeConsole.println("Invalid list mode for " + type);
            QingkeConsole.println("Please choose one of ALL|MINE|OPEN|ID for listing questions");
            return null;
        }
        
        List<Question> questions = new ArrayList<Question>();
        if ("ALL".equalsIgnoreCase(type)) {
            questions = dao.getQuestions();
        } else if ("MINE".equalsIgnoreCase(type)) {
            questions = dao.getQuestionsFrom(playerProfile);
        } else if ("OPEN".equalsIgnoreCase(type)) {
            List<Question> allQuestions = dao.getQuestions();
            for (Question question : allQuestions) {
                if (question.isOpen()) {
                    questions.add(question);
                }
            }
        } else if ("ID".equalsIgnoreCase(type)) {
            long qid = QingkeConsole.askUserNumber("Question ID: ");
            Question question = dao.getQuestion(qid);
            questions.add(question);
        }

        for (Question question : questions) {
            QingkeConsole.println(question);
            
            // list answers for ID mode
            if ("ID".equalsIgnoreCase(type)) {
                QingkeConsole.println("=============== Answers ===============");
                for (Answer answer : question.getAnswers()) {
                    QingkeConsole.println(answer);
                }
            }
        }
        
        return null;
    }
}
