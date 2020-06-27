package me.blazingtide.zetsu.tabcomplete.listener;

import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@AllArgsConstructor
public class TabCompleteListener implements TabCompleter {

    private final TabCompleteHandler handler;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command ignored, String label, String[] args) {
        //Sub commands first.

        if (args.length == 0) { //It's a bit more difficult to process tab completes for a command like /<command> <sub1> <sub2> <arguments>, so it's harder to tab complete <sub2>

            return null;
        }

        return null;
    }
}
