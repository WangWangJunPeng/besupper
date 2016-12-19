package com.qingke.easyjava.jdbcapp.pojo;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private long id;
    private String name;
    private int score;

    private List<Question> questions;
    private List<Answer> answers;
    
    public Player(){
    }
    
    public Player(long id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
        
        this.questions = new ArrayList<Question>();
        this.answers = new ArrayList<Answer>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
