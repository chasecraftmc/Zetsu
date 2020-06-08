package me.blazingtide.zetsu;

import me.blazingtide.zetsu.schema.annotations.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final Zetsu zetsu = new Zetsu(this);

        zetsu.registerCommands(this);
    }

    @Command(labels = {"run test"}, description = "idk", async = true)
    public void execute(CommandSender sender, String args) {
        sender.sendMessage(ChatColor.BLUE + "YOU RAN THIS COMMAND YAY! (ARGS: " + args + ")");
    }

    @Command(labels = {"run test2 idk"}, description = "idk", async = true)
    public void execute(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "YOU RAN THIS COMMAND YAY!");
    }

}
