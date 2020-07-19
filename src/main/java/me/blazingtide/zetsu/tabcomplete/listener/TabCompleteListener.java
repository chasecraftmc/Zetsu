package me.blazingtide.zetsu.tabcomplete.listener;

import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@AllArgsConstructor
public class TabCompleteListener implements TabCompleter {

    private final TabCompleteHandler handler;
    private final CommandProcessor processor;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command ignored, String label, String[] args) {
        //Sub commands first.

        final CachedCommand command = processor.find(label, args);

        if (command == null) {
            return null; //should not happen but just incase
        }

        //It's a bit more difficult to process tab completes for a command like /<command> <sub1> <sub2> <arguments>, so it's harder to tab complete <sub2>
        if (args.length == command.getArgs().size()) {
            final List<String> toReturn = handler.requestSubcommands(label);
            return toReturn.isEmpty() ? null : toReturn;
        }

        return null; //Will finish up later
    }
}
