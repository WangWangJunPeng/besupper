package com.qingke.easyjava.jdbcapp.command;

import java.util.HashMap;
import java.util.Map;

import com.qingke.easyjava.jdbcapp.InvalidInputException;
import com.qingke.easyjava.jdbcapp.command.impl.AnswerCommand;
import com.qingke.easyjava.jdbcapp.command.impl.AskCommand;
import com.qingke.easyjava.jdbcapp.command.impl.ExitCommand;
import com.qingke.easyjava.jdbcapp.command.impl.HelpCommand;
import com.qingke.easyjava.jdbcapp.command.impl.ListCommand;
import com.qingke.easyjava.jdbcapp.command.impl.LoginCommand;
import com.qingke.easyjava.jdbcapp.command.impl.ScoreCommand;
import com.qingke.easyjava.jdbcapp.command.impl.SignupCommand;
import com.qingke.easyjava.jdbcapp.pojo.Player;

public class CommandFactory {
    
    public enum CommandCode {
        //login and sign up account
        LOGIN,
        SIGNUP,

        // command to play game
        LIST,
        ASK,
        ANSWER,
        SCORE,

        // accept answer
        ACCEPT,

        // SYSTEM
        HELP,
        EXIT
    }

    private static CommandFactory instance;//懒汉式单例模式
    
    public static CommandFactory getInstance(){
        if (instance == null) {
            instance = new CommandFactory();
        }
        
        return instance;
    }

    private Map<CommandCode, Class<? extends Command>> commandMap
        = new HashMap<CommandCode, Class<? extends Command>>();

    private CommandFactory() {
        commandMap.put(CommandCode.EXIT, ExitCommand.class);
        commandMap.put(CommandCode.HELP, HelpCommand.class);
        commandMap.put(CommandCode.LOGIN, LoginCommand.class);
        commandMap.put(CommandCode.SIGNUP, SignupCommand.class);
        commandMap.put(CommandCode.LIST, ListCommand.class);
        commandMap.put(CommandCode.ASK, AskCommand.class);
        commandMap.put(CommandCode.ANSWER, AnswerCommand.class);
        commandMap.put(CommandCode.SCORE, ScoreCommand.class);
    }

    public Command buildCommand(CommandCode type, Player player) {
        Class<? extends Command> commandClass = commandMap.get(type);
        
        if (commandClass == null) {
            throw new InvalidInputException("Invalid command: " + type.name());
        }

        try {
            Command command = commandClass.newInstance();
            if (command instanceof PlayerCommand) {
                PlayerCommand playerCommand = (PlayerCommand)command;
                playerCommand.setPlayer(player);
                return playerCommand;
            }
            return command;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidInputException("Unable to reflect a new command instance, due to "
                    + e.getMessage());
        }
    }
}
