package me.blazingtide.zetsu.processor.impl;

import com.google.common.collect.Lists;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.annotations.parameter.Default;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class SpigotProcessor extends CommandProcessor implements CommandExecutor {

    public static int MAX_ELEMENTS_HELP_PAGE = 10; //Public & not final just incase someone wants to change this in the future.

    public SpigotProcessor(Zetsu zetsu) {
        super(zetsu);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final CachedCommand found = this.find(label, args);

        if (found == null) {
            int page = 1;

            if (args.length != 0) {
                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                } //ignore
            }

            sendHelpMessage(label, page, sender);
            return false;
        }

        int defaults = 0;

        for (Parameter parameter : found.getMethod().getParameters()) {
            defaults += parameter.isAnnotationPresent(Default.class) ? 1 : 0;
        }

        for (int i = 0; i < found.getArgs().size() + defaults; i++) {
            if (args.length > i) {
                args = (String[]) ArrayUtils.remove(args, 0);
            }
        }

        this.invoke(found, args, sender);
        return false;
    }

    private void sendHelpMessage(String label, int page, CommandSender sender) {
        final ArrayList<String> commands = Lists.newArrayList();

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------");
        sender.sendMessage(ChatColor.GOLD + StringUtils.capitalize(label) + ChatColor.GRAY + " -" + ChatColor.WHITE + " (Command Help)");
        sender.sendMessage(" ");

        for (CachedCommand command : zetsu.getLabelMap().get(label)) {
            final Method method = command.getMethod();
            final StringBuilder builder = new StringBuilder();

            for (int i = 1; i < method.getParameters().length; i++) {
                final Parameter parameter = method.getParameters()[i];

                if (parameter.isAnnotationPresent(Param.class)) {
                    builder.append("<").append(parameter.getAnnotation(Param.class).value()).append("> ");
                } else {
                    builder.append("<").append(parameter.getType().getSimpleName()).append("> ");
                }
            }

            commands.add(" " + ChatColor.YELLOW + "/" + label + " " + String.join(" ", command.getArgs()) + " " + builder.toString().trim() + ChatColor.GRAY + " - " + ChatColor.WHITE + command.getDescription());
        }

        int start = (page - 1) * MAX_ELEMENTS_HELP_PAGE;
        int end = start + MAX_ELEMENTS_HELP_PAGE;

        for (int i = start; i < end; i++) {
            if (commands.size() <= i) {
                continue;
            }
            sender.sendMessage(commands.get(i));
        }

        int maxPage = commands.size() / MAX_ELEMENTS_HELP_PAGE + 1;

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.GOLD + "You're on page " + ChatColor.WHITE + page + ChatColor.GOLD + " of " + ChatColor.WHITE + maxPage + ChatColor.GOLD + ".");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------");
    }

}
