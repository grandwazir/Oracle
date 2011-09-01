
package name.richardson.james.oracle;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;

import name.richardson.james.oracle.cache.CachedPattern;
import name.richardson.james.oracle.commands.AskCommand;
import name.richardson.james.oracle.commands.ReloadCommand;
import name.richardson.james.oracle.configurations.PatternConfiguration;
import name.richardson.james.oracle.exceptions.InvalidPatternNodeException;
import name.richardson.james.oracle.exceptions.UnableToCreateConfigurationException;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Oracle extends JavaPlugin {

  private ResourceBundle messages;
  public List<CachedPattern> cachedPatterns = new LinkedList<CachedPattern>();
  
  private final static Locale locale = Locale.getDefault();
  private final static Logger logger = Logger.getLogger("Minecraft");

  public Configuration patternConfiguration;

  private CommandManager commandManager;
  private PluginDescriptionFile description;
  private PluginManager pluginManager;

  public Oracle() {
    if (messages == null) {
      try {
        messages = ResourceBundle.getBundle("name.richardson.james.oracle.localisation.Messages", locale);
      } catch (final MissingResourceException e) {
        messages = ResourceBundle.getBundle("name.richardson.james.oracle.localisation.Messages");
        log(Level.WARNING, String.format(getMessage("noLocalisationFound"), locale.getDisplayLanguage()));
      }
    }
  }
  
  public void log(final Level level, final String message) {
    logger.log(level, "[Oracle] " + message);
  }

  @Override
  public void onDisable() {
    log(Level.INFO, String.format(getMessage("pluginDisabled"), description.getName()));
  }

  @Override
  public void onEnable() {
    description = getDescription();
    pluginManager = getServer().getPluginManager();
    commandManager = new CommandManager(this);
    
    getCommand("oracle").setExecutor(commandManager);
    commandManager.registerCommand("ask", new AskCommand(this));
    commandManager.registerCommand("reload", new ReloadCommand(this));
    
    try {
      patternConfiguration = new PatternConfiguration(new File("plugins/Oracle/patterns.yml"), this);
    } catch (UnableToCreateConfigurationException e) {
      log(Level.SEVERE, String.format(getMessage("UnableToCreateConfigurationException"), e.getPath()));
      pluginManager.disablePlugin(this);
      return;
    }
    
    cachePatterns();
   
    log(Level.INFO, String.format(getMessage("pluginEnabled"), description.getFullName()));
  }
  
  public String getMessage(final String key) {
    return messages.getString(key);
  }

  public PluginManager getPluginManager() {
    return pluginManager;
  }

  public void cachePatterns() {
    cachedPatterns.clear();
    for (String node : patternConfiguration.getKeys()) {
      try {
        String pattern = patternConfiguration.getString(node + ".pattern");
        String message = patternConfiguration.getString(node + ".message");
        CachedPattern cachedPattern = new CachedPattern(pattern, message);
        cachedPatterns.add(cachedPattern);
      } catch (PatternSyntaxException e) {
        log(Level.WARNING, String.format(getMessage("PatternSyntaxException"), node));
      } catch (InvalidPatternNodeException e) {
        log(Level.WARNING, String.format(getMessage("InvalidPatternNodeException"), node));
        patternConfiguration.removeProperty(node);
      }
    }
    log(Level.INFO, String.format(getMessage("patternsLoaded"), cachedPatterns.size()));
    patternConfiguration.save();
  }
  
  public void sendFormattedMessage(CommandSender sender, String message) {
    message = message.replace("&", "§");
    
    for (String line : message.split("/n")) {
      sender.sendMessage(line);
    }
    
  }

}
