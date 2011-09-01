package name.richardson.james.oracle.configurations;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import name.richardson.james.oracle.Oracle;
import name.richardson.james.oracle.exceptions.UnableToCreateConfigurationException;



public class PatternConfiguration extends Configuration {

  public PatternConfiguration(File file, Oracle plugin) throws UnableToCreateConfigurationException {
    super(file, plugin);
  }

  @Override
  void setDefaults(File file) throws UnableToCreateConfigurationException {
    try {
      file.getParentFile().mkdirs();
      file.createNewFile();
      this.getString("examplePattern", "");
      this.getString("examplePattern.pattern", "[oracle]");
      this.getBoolean("examplePattern.requireCompleteMatch", false);
      this.getString("examplePattern.message", "To ask a question just type /oracle ask");
      this.save();
    } catch (final IOException e) {
      throw new UnableToCreateConfigurationException(file.getPath());
    }
  }
  
  

}
