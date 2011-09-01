
package name.richardson.james.oracle.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import name.richardson.james.oracle.Oracle;
import name.richardson.james.oracle.exceptions.NotEnoughArgumentsException;
import name.richardson.james.oracle.exceptions.PlayerNotAuthorisedException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class Command implements CommandExecutor {

  protected String className;
  protected String description;
  protected String name;
  protected String permission;
  protected Oracle plugin;
  protected String usage;

  public Command(final Oracle plugin) {
    super();
    this.plugin = plugin;
    className = this.getClass().getSimpleName();
    name = this.plugin.getMessage(className + "Name");
    description = this.plugin.getMessage(className + "Description");
    usage = this.plugin.getMessage(className + "Usage");
    permission = "oracle" + "." + name;
  }

  public abstract void execute(CommandSender sender, Map<String, String> arguments);

  public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args) {
    try {
      final LinkedList<String> arguments = new LinkedList<String>();
      arguments.addAll(Arrays.asList(args));
      final Map<String, String> parsedArguments = parseArguments(arguments);
      execute(sender, parsedArguments);
    } catch (final NotEnoughArgumentsException e) {
      sender.sendMessage(ChatColor.RED + plugin.getMessage("NotEnoughArgumentsException"));
      sender.sendMessage(ChatColor.YELLOW + usage);
    }
    return true;
  }

  protected void authorisePlayer(final CommandSender sender, String node) throws PlayerNotAuthorisedException {
    node = node.toLowerCase();
    if (sender instanceof ConsoleCommandSender) {
      return;
    } else {
      final Player player = (Player) sender;
      if (player.hasPermission(node) || player.hasPermission("oracle.*")) { return; }
    }
    throw new PlayerNotAuthorisedException();
  }
  
  protected abstract Map<String, String> parseArguments(List<String> arguments) throws NotEnoughArgumentsException;

  protected void registerPermission(final String name, final String description, final PermissionDefault defaultValue) {
    final Permission permission = new Permission(name, description, defaultValue);
    plugin.getPluginManager().addPermission(permission);
  }

}
