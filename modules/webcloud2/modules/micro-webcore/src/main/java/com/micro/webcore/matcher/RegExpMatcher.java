package com.micro.webcore.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpMatcher implements PatternMatcher {

  @Override
  public boolean matches(String pattern, String source) {
    if (pattern == null) {
      throw new IllegalArgumentException("pattern argument cannot be null.");
    }
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(source);
    return m.matches();
  }
}