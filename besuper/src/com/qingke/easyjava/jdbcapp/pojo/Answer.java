package com.qingke.easyjava.jdbcapp.pojo;

public class Answer {

    private long id;
    private String value;
    private Player from;
    private boolean bestAnswer;
    private long questionId;

    public Answer(long id, String value, Player from) {
        this.id = id;
        this.value = value;
        this.from = from;
        
        this.bestAnswer = false;
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

    public boolean isBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(boolean bestAnswer) {
        this.bestAnswer = bestAnswer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String toString() {
        String template = "%s%d. %s [QID: %d] [%s]";
        String isBest = bestAnswer ? "[BEST] " : "";

        return String.format(template, isBest, id, value, questionId, from.getName());
    }
}
