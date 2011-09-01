package name.richardson.james.oracle.cache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import name.richardson.james.oracle.exceptions.InvalidPatternNodeException;


public class CachedPattern {

  private Pattern pattern;
  private String message;

  public CachedPattern (String pattern, String message) throws PatternSyntaxException, InvalidPatternNodeException {
    if (pattern == null || message == null) throw new InvalidPatternNodeException();
    this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    this.message = message;
  }
  
  public boolean matches(String message) {
    Matcher matcher = pattern.matcher(message);
    matcher.matches();
    return matcher.find();
  }

  public String getMessage() {
    return message;
  }
  
  public String getPattern() {
    return this.pattern.toString();
  }
  
}
