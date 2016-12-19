package com.qingke.easyjava.jdbcapp.pojo;

public class PlayerCredential extends Player {

    private String username;
    private String password;
    
    public PlayerCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public PlayerCredential(long id, String name, int score, String username, String password) {
        super(id, name, score);
        
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player narrow(){
        Player player = new Player(this.getId(), this.getName(), this.getScore());

        player.setQuestions(this.getQuestions());
        player.setAnswers(this.getAnswers());
        
        return player;
    }
}
