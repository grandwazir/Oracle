package name.richardson.james.oracle.configurations;

import java.io.File;
import java.util.logging.Level;

import name.richardson.james.oracle.Oracle;
import name.richardson.james.oracle.exceptions.UnableToCreateConfigurationException;


public abstract class Configuration extends org.bukkit.util.config.Configuration {

  protected final Oracle plugin;
  protected File file;

  public Configuration(File file, Oracle plugin) throws UnableToCreateConfigurationException {
    super(file);
    this.plugin = plugin;
    this.file = file;
    this.checkExistance();
  }

  abstract void setDefaults(File file) throws UnableToCreateConfigurationException;
  
  protected boolean isEmpty() {
    return this.getAll().isEmpty();
  }
  
  void checkExistance() throws UnableToCreateConfigurationException {
    this.load();
    if (isEmpty()) {
      this.plugin.log(Level.WARNING, String.format(plugin.getMessage("configurationNotFound"), this.getClass().getSimpleName()));
      this.plugin.log(Level.INFO, String.format(plugin.getMessage("configurationCreation"), this.getClass().getSimpleName(), file.getPath()));
      setDefaults(file);
    } else {
      this.plugin.log(Level.INFO, String.format(plugin.getMessage("configurationLoaded"), this.getClass().getSimpleName(), file.getPath()));
    }
  }
  
}
