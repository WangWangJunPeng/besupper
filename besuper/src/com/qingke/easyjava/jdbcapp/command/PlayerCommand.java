package com.qingke.easyjava.jdbcapp.command;

import com.qingke.easyjava.jdbcapp.InvalidInputException;
import com.qingke.easyjava.jdbcapp.pojo.Player;

public abstract class PlayerCommand implements Command {

    //======================= fields
    private Player playerProfile;

    //======================= public methods
    @Override
    public Object execute() {
        validatePlayerProfile();

        return execute(playerProfile);
    }

    //======================= private methods
    private void validatePlayerProfile(){
        if (playerProfile == null) {
            throw new InvalidInputException("Please Login/Signup first!");
        }
    }

    //======================= abstract method
    protected abstract Object execute(Player player);

    //======================= getters and setters
    public void setPlayer(Player player) {
        this.playerProfile = player;
    }
}
