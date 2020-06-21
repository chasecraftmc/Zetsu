# Zetsu
Extensive Command API for the Spigot API

### Installation
1. Retrieve the jar in ``/target/`` or compile the plugin via git and maven.
2. Add the project as a maven dependency or put it in your build path.
3. Start using the API!

### Usage

```java
private Zetsu zetsu

@Override
public void onEnable() {
    zetsu = new Zetsu(this);
    
    zetsu.registerCommands(this);
}

@Command(labels = {"example"})
public void execute(CommandSender sender, String exampleString) {
    sender.sendMessage(exampleString);
}

@Command(labels = {"example subcommand"})
@Permissible("permission.admin") //Player requires the permission node "permission.admin" to perform this command
public void executeSubCommand(Player player) { //Player only command
    player.sendMessage("You're a player!");
} 

```

### Contact
**Telegram**: @BlazingTide
**Discord**:  BlazingTide#3817
