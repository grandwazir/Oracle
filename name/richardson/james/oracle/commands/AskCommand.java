
package name.richardson.james.oracle.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


import name.richardson.james.oracle.Oracle;
import name.richardson.james.oracle.cache.CachedPattern;
import name.richardson.james.oracle.exceptions.NotEnoughArgumentsException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class AskCommand extends Command {

  public AskCommand(final Oracle plugin) {
    super(plugin);
    registerPermission(permission, plugin.getMessage(className + "PermissionDescription"), PermissionDefault.TRUE);
  }

  @Override
  public void execute(final CommandSender sender, final Map<String, String> arguments) {
    for (CachedPattern pattern : plugin.cachedPatterns) {
      if (pattern.matches(arguments.get("question"))) {
        String message = pattern.getMessage();
        plugin.sendFormattedMessage(sender, message);
        return;
      }
    }
    sender.sendMessage(ChatColor.RED + plugin.getMessage("noMatchFound")); 
  }

  @Override
  protected Map<String, String> parseArguments(final List<String> arguments) throws NotEnoughArgumentsException {
    final Map<String, String> m = new HashMap<String, String>();
    StringBuilder question = new StringBuilder();
    arguments.remove(0);
    
    for (String argument : arguments) {
      question.append(argument + " ");
    }
    
    m.put("question", question.toString());
    
    if (m.get("question").isEmpty()) {
      throw new NotEnoughArgumentsException();
    } else {
      return m;
    }
    
  }

}
