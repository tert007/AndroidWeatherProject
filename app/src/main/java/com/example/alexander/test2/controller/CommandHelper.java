package com.example.alexander.test2.controller;

import com.example.alexander.test2.controller.impl.UpdateForecastCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 11.03.2016.
 */
public class CommandHelper {
    private Map<CommandName, Command> commands = new HashMap<>();

    public CommandHelper(){
        commands.put(CommandName.UPDATE_FORECAST, new UpdateForecastCommand()) ;
    }

    public Command getCommand(CommandName commandName) {
        return commands.get(commandName);
    }
}
