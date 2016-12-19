package com.qingke.easyjava.jdbcapp;

import java.util.ArrayList;
import java.util.List;

import com.qingke.easyjava.jdbcapp.command.Command;
import com.qingke.easyjava.jdbcapp.command.CommandFactory;
import com.qingke.easyjava.jdbcapp.command.CommandFactory.CommandCode;
import com.qingke.easyjava.jdbcapp.pojo.Answer;
import com.qingke.easyjava.jdbcapp.pojo.Player;
import com.qingke.easyjava.jdbcapp.pojo.PlayerCredential;
import com.qingke.easyjava.jdbcapp.pojo.Question;
import com.qingke.easyjava.jdbcapp.util.QingkeConsole;

public class BeSuperApplication {

    private static BeSuperDao dao = new BeSuperDao();
    private static Player playerProfile;

    public static void main(String[] args) {
        // start
        QingkeConsole.println("You can use the \"HELP\" for command usage. Have fun!");
        CommandFactory factory = CommandFactory.getInstance();
        while (true) {
            try {
                String cmd = QingkeConsole.askUserInput("cmd> ");

                // using Factory pattern
                CommandCode commandCode = CommandCode.valueOf(cmd.toUpperCase());
                Command command = factory.buildCommand(commandCode, playerProfile);

                Object result = command.execute();
                if (result != null && result instanceof Player) {
                    playerProfile = (Player)result;
                }

                // not using Factory pattern
//                switch (CommandCode.valueOf(cmd.toUpperCase())) {
//                case EXIT:
//                    doExit();
//                    break;
//                case HELP:
//                    doPrintUsage();
//                    break;
//                case LOGIN:
//                    doLogin();
//                    break;
//                case SIGNUP:
//                    doSignup();
//                    break;
//                case LIST:
//                    doList();
//                    break;
//                case ASK:
//                    doAsk();
//                    break;
//                case ANSWER:
//                    doAnswer();
//                    break;
//                case ACCEPT:
//                    doAccept();
//                    break;
//                case SCORE:
//                    doShowScore();
//                    break;
//                default:
//                    doPrintUsage();
//                    break;
//                }
//                
            } catch (InvalidInputException e) {
                QingkeConsole.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                QingkeConsole.println("Unsupported command!");
            } catch (Exception e) {
                QingkeConsole.println("System Error! " + e.getMessage());
            }
        }
    }

    public static Player getPlayerProfile(){
        return playerProfile;
    }

    private static void doExit(){
        QingkeConsole.terminate();
    }
    
    private static void doPrintUsage(){
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
    }
    
    private static void doLogin() {
        String username = QingkeConsole.askUserInput("Username: ");
        String password = QingkeConsole.askUserInput("Password: ");
        
        PlayerCredential credential = new PlayerCredential(username, password);
        Player player = dao.login(credential);

        if (player != null) {
            List<Question> myQuestions = dao.getQuestionsFrom(player);
            List<Answer> myAnswers = dao.getAnswersFrom(player);

            playerProfile = player;
            playerProfile.setQuestions(myQuestions);
            playerProfile.setAnswers(myAnswers);
            
            QingkeConsole.println("Welcome back, " + playerProfile.getName() + "! Type 'help' for command usage.");
        } else {
            QingkeConsole.println("Username/Password is not valid!");
        }
    }
    
    private static void doSignup() {
        String username = QingkeConsole.askUserInput("Username: ");
        String password = QingkeConsole.askUserInput("Password: ");
        String name = QingkeConsole.askUserInput("Display Name: ", true);
        if ("".equalsIgnoreCase(name)) {
            name = username;
        }
        
        PlayerCredential credential = new PlayerCredential(-1, name, 0, username, password);
        Player player = dao.signup(credential);
        
        if (player != null) {
            playerProfile = player;
        }
    }

    private static void doList() {
        validateUserLogin();

        String type = QingkeConsole.askUserInput("List questions for (ALL|MINE|OPEN|ID): ");
        if (!"ALL|MINE|OPEN|ID|ANSWERS".contains(type.toUpperCase())) {
            QingkeConsole.println("Invalid list mode for " + type);
            QingkeConsole.println("Please choose one of ALL|MINE|OPEN|ID for listing questions");
            return;
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
    }
    
    private static void doAsk() {
        validateUserLogin();
        
        String value = QingkeConsole.askUserInput("Your qusetion�� ");
        int credit = QingkeConsole.askUserNumberWithDefault("Credit [optional, Integer only]: ", 0);
        if (credit > playerProfile.getScore()) {
            QingkeConsole.println("Insufficient credit! Reset credits to your remains: " + playerProfile.getScore());
            credit = playerProfile.getScore();
        }

        Question question = new Question(-1, value, playerProfile, credit);
        playerProfile.getQuestions().add(question);
        playerProfile.setScore(playerProfile.getScore() - credit);
        
        dao.upsertQuestion(question);
        dao.updatePlayer(playerProfile);
    }

    private static void doAnswer() {
        validateUserLogin();

        int qid = QingkeConsole.askUserNumber("Question ID: ");
        Question question = dao.getQuestion(qid);

        if (question == null) {
            QingkeConsole.println("Invalid question id! id: " + qid);
            return;
        }
        
        if (!question.isOpen()) {
            QingkeConsole.println("The question is resolved. Try another one.");
            return;
        }

        if (playerProfile.getQuestions().contains(question)) {
            QingkeConsole.println("You are not allowed to answer the quetion from yourself!");
            return;
        }

        String value = QingkeConsole.askUserInput("Enter your answer: ");
        Answer answer = new Answer(-1, value, playerProfile);
        answer.setQuestionId(qid);

        dao.upsertAnwser(answer);
    }

    private static void doAccept(){
        validateUserLogin();

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
            return;
        }

        int score = myQuestion.getCredit();
        score = (score <= 0) ? 1 : score;

        Answer bestAnswer = null;
        for (Answer answer :myQuestion.getAnswers()){
            if (answer.getId() == aid) {
                bestAnswer = answer;
                break;
            }
        }

        if (bestAnswer == null) {
            QingkeConsole.println("Answer(ID: " + aid + " is not exist or not belong to the question.");
            return;
        }

        Player player = bestAnswer.getFrom();
        player.setScore(player.getScore() + score);

        myQuestion.setBestAnswer(bestAnswer);
        bestAnswer.setBestAnswer(true);

        dao.acceptAnswer(myQuestion, bestAnswer);
        dao.updatePlayer(player);
    }

    private static void doShowScore() {
        validateUserLogin();

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
        
        QingkeConsole.println("Your current Level�� " + level);
        
    }
    
    private static void validateUserLogin() {
        if (playerProfile == null) {
            throw new InvalidInputException("Please Login/Signup first!");
        }
    }
}
