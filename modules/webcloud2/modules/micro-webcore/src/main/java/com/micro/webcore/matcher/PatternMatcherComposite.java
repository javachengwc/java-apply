package com.micro.webcore.matcher;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class PatternMatcherComposite implements  PatternMatcher {

  private final List<PatternMatcher> delegates = new ArrayList<PatternMatcher>();

  public void addPatternMatcher(List<PatternMatcher> matchers) {
    if (!CollectionUtils.isEmpty(matchers)) {
      this.delegates.addAll(matchers);
    }
  }

  public void addPatternMatcher(PatternMatcher matcher) {
    this.delegates.add(matcher);
  }

  @Override
  public boolean matches(String pattern, String source) {
    for (PatternMatcher delegate : this.delegates) {
      boolean match =delegate.matches(pattern,source);
      if(match) {
        return match;
      }
    }
    return false;
  }


}
