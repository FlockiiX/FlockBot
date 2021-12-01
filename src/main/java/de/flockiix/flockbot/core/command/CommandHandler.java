package de.flockiix.flockbot.core.command;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.exception.CommandAlreadyExistsException;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);
    /**
     * ArrayList with all registered commands
     */
    private final List<Command> commands = new ArrayList<>();

    /**
     * Registers the given commands.
     *
     * @param commandsToRegister the commands to be registered
     */
    public void registerCommands(Command... commandsToRegister) {
        List<Command> toRegisterCommands = new ArrayList<>(Arrays.asList(commandsToRegister));

        for (Command command : toRegisterCommands) {
            boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));
            if (nameFound)
                throw new CommandAlreadyExistsException("A command with this name is already present: " + command.getName());

            boolean aliasFound = this.commands.stream().anyMatch((it) -> it.getAliases().stream().anyMatch((alias) -> command.getAliases().contains(alias)));
            if (aliasFound)
                throw new CommandAlreadyExistsException("A command with a alias is already present: " + command.getName() + " - " + command.getAliases());

            commands.add(command);
        }

        registerSlashCommands();
        LOGGER.info("Command registration finished");
    }

    /**
     * Registers all commands in the list with commands as SlashCommands.
     */
    private void registerSlashCommands() {
        CommandListUpdateAction listUpdateAction = BotInfo.jda.updateCommands();
        for (Command command : getCommands()) {
            CommandData commandData = new CommandData(command.getName(), command.getDescription()).addOptions(command.getOptions());
            if (command.getCommandCategory() == CommandCategory.DEVELOPER || command.getCommandCategory() == CommandCategory.HIDDEN)
                continue;
            listUpdateAction.addCommands(commandData);
        }

        listUpdateAction.queue();
    }

    /**
     * Searches for a command with the given name/alias
     *
     * @param search the name/alias of the command
     * @return the command if found by its name or aliases. Otherwise null
     */
    public Command getCommand(String search) {
        String searchLower = search.toLowerCase();
        return this.commands.stream()
                .filter(cmd -> cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)).findFirst().orElse(null);
    }

    /**
     * Gets all registered commands and returns them.
     *
     * @return a list with all registered commands
     */
    public List<Command> getCommands() {
        return commands;
    }
}