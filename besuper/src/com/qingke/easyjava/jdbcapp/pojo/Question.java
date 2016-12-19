package com.qingke.easyjava.jdbcapp.pojo;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private long id;
    private String value;
    private Player from;
    private int credit;
    private boolean open;

    private List<Answer> answers;
    private Answer bestAnswer;

    public Question(long id, String value, Player from, int credit) {
        this.id = id;
        this.value = value;
        this.from = from;
        this.credit = credit;
        this.open = true;
        
        this.answers = new ArrayList<Answer>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(Answer bestAnswer) {
        this.bestAnswer = bestAnswer;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String toString() {
        // sample: [SOLVED] 1: WHAT IS YOUR NAME? [5 credits] [2 answers]
        String template = "[%s] %d: [%s] [%d credits] [%d answers]";
        String solved = open ? "SOLVED" : " UNSOLVED ";
        return String.format(template, solved, id, value, credit, answers.size());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj != null) {
            Question qObj = (Question) obj;
            return this.id == qObj.getId();
        }
        return false;
    }
}
