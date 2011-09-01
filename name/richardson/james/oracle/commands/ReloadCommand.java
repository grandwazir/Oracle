
package name.richardson.james.oracle.commands;

import java.util.List;
import java.util.Map;


import name.richardson.james.oracle.Oracle;
import name.richardson.james.oracle.exceptions.NotEnoughArgumentsException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class ReloadCommand extends Command {

  public ReloadCommand(final Oracle plugin) {
    super(plugin);
    registerPermission(permission, plugin.getMessage(className + "PermissionDescription"), PermissionDefault.TRUE);
  }

  @Override
  public void execute(final CommandSender sender, final Map<String, String> arguments) {
    plugin.patternConfiguration.load();
    plugin.cachePatterns();
    sender.sendMessage(ChatColor.GREEN + plugin.getMessage("configurationReloaded")); 
  }

  @Override
  protected Map<String, String> parseArguments(final List<String> arguments) throws NotEnoughArgumentsException {
    return null;
  }

}
