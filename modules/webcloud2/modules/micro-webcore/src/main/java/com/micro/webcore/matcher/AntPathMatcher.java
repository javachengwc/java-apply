package com.micro.webcore.matcher;

import org.springframework.util.StringUtils;

public class AntPathMatcher implements PatternMatcher {

  public static final String DEFAULT_PATH_SEPARATOR = "/";

  private String pathSeparator = DEFAULT_PATH_SEPARATOR;

  public void setPathSeparator(String pathSeparator) {
    this.pathSeparator = (pathSeparator != null ? pathSeparator : DEFAULT_PATH_SEPARATOR);
  }


  public boolean isPattern(String path) {
    return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
  }

  @Override
  public boolean matches(String pattern, String source) {
    return match(pattern, source);
  }

  public boolean match(String pattern, String path) {
    return doMatch(pattern, path, true);
  }

  public boolean matchStart(String pattern, String path) {
    return doMatch(pattern, path, false);
  }

  protected boolean doMatch(String pattern, String path, boolean fullMatch) {
    if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
      return false;
    }

    String[] pattDirs = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator);
    String[] pathDirs = StringUtils.tokenizeToStringArray(path, this.pathSeparator);

    int pattIdxStart = 0;
    int pattIdxEnd = pattDirs.length - 1;
    int pathIdxStart = 0;
    int pathIdxEnd = pathDirs.length - 1;

    // Match all elements up to the first **
    while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      String patDir = pattDirs[pattIdxStart];
      if ("**".equals(patDir)) {
        break;
      }
      if (!matchStrings(patDir, pathDirs[pathIdxStart])) {
        return false;
      }
      pattIdxStart++;
      pathIdxStart++;
    }

    if (pathIdxStart > pathIdxEnd) {
      // Path is exhausted, only match if rest of pattern is * or **'s
      if (pattIdxStart > pattIdxEnd) {
        return (pattern.endsWith(this.pathSeparator) ?
            path.endsWith(this.pathSeparator) : !path.endsWith(this.pathSeparator));
      }
      if (!fullMatch) {
        return true;
      }
      if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") &&
          path.endsWith(this.pathSeparator)) {
        return true;
      }
      for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
        if (!pattDirs[i].equals("**")) {
          return false;
        }
      }
      return true;
    } else if (pattIdxStart > pattIdxEnd) {
      // String not exhausted, but pattern is. Failure.
      return false;
    } else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
      // Path start definitely matches due to "**" part in pattern.
      return true;
    }

    // up to last '**'
    while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      String patDir = pattDirs[pattIdxEnd];
      if (patDir.equals("**")) {
        break;
      }
      if (!matchStrings(patDir, pathDirs[pathIdxEnd])) {
        return false;
      }
      pattIdxEnd--;
      pathIdxEnd--;
    }
    if (pathIdxStart > pathIdxEnd) {
      // String is exhausted
      for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
        if (!pattDirs[i].equals("**")) {
          return false;
        }
      }
      return true;
    }

    while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
      int patIdxTmp = -1;
      for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
        if (pattDirs[i].equals("**")) {
          patIdxTmp = i;
          break;
        }
      }
      if (patIdxTmp == pattIdxStart + 1) {
        // '**/**' situation, so skip one
        pattIdxStart++;
        continue;
      }
      // Find the pattern between padIdxStart & padIdxTmp in str between
      // strIdxStart & strIdxEnd
      int patLength = (patIdxTmp - pattIdxStart - 1);
      int strLength = (pathIdxEnd - pathIdxStart + 1);
      int foundIdx = -1;

      strLoop:
      for (int i = 0; i <= strLength - patLength; i++) {
        for (int j = 0; j < patLength; j++) {
          String subPat = (String) pattDirs[pattIdxStart + j + 1];
          String subStr = (String) pathDirs[pathIdxStart + i + j];
          if (!matchStrings(subPat, subStr)) {
            continue strLoop;
          }
        }
        foundIdx = pathIdxStart + i;
        break;
      }

      if (foundIdx == -1) {
        return false;
      }

      pattIdxStart = patIdxTmp;
      pathIdxStart = foundIdx + patLength;
    }

    for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
      if (!pattDirs[i].equals("**")) {
        return false;
      }
    }

    return true;
  }

  private boolean matchStrings(String pattern, String str) {
    char[] patArr = pattern.toCharArray();
    char[] strArr = str.toCharArray();
    int patIdxStart = 0;
    int patIdxEnd = patArr.length - 1;
    int strIdxStart = 0;
    int strIdxEnd = strArr.length - 1;
    char ch;

    boolean containsStar = false;
    for (char aPatArr : patArr) {
      if (aPatArr == '*') {
        containsStar = true;
        break;
      }
    }

    if (!containsStar) {
      // No '*'s, so we make a shortcut
      if (patIdxEnd != strIdxEnd) {
        return false; // Pattern and string do not have the same size
      }
      for (int i = 0; i <= patIdxEnd; i++) {
        ch = patArr[i];
        if (ch != '?') {
          if (ch != strArr[i]) {
            return false;// Character mismatch
          }
        }
      }
      return true; // String matches against pattern
    }


    if (patIdxEnd == 0) {
      return true; // Pattern contains only '*', which matches anything
    }

    // Process characters before first star
    while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
      if (ch != '?') {
        if (ch != strArr[strIdxStart]) {
          return false;// Character mismatch
        }
      }
      patIdxStart++;
      strIdxStart++;
    }
    if (strIdxStart > strIdxEnd) {
      // All characters in the string are used. Check if only '*'s are
      // left in the pattern. If so, we succeeded. Otherwise failure.
      for (int i = patIdxStart; i <= patIdxEnd; i++) {
        if (patArr[i] != '*') {
          return false;
        }
      }
      return true;
    }

    // Process characters after last star
    while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
      if (ch != '?') {
        if (ch != strArr[strIdxEnd]) {
          return false;// Character mismatch
        }
      }
      patIdxEnd--;
      strIdxEnd--;
    }
    if (strIdxStart > strIdxEnd) {
      // All characters in the string are used. Check if only '*'s are
      // left in the pattern. If so, we succeeded. Otherwise failure.
      for (int i = patIdxStart; i <= patIdxEnd; i++) {
        if (patArr[i] != '*') {
          return false;
        }
      }
      return true;
    }

    // process pattern between stars. padIdxStart and patIdxEnd point
    // always to a '*'.
    while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
      int patIdxTmp = -1;
      for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
        if (patArr[i] == '*') {
          patIdxTmp = i;
          break;
        }
      }
      if (patIdxTmp == patIdxStart + 1) {
        // Two stars next to each other, skip the first one.
        patIdxStart++;
        continue;
      }
      // Find the pattern between padIdxStart & padIdxTmp in str between
      // strIdxStart & strIdxEnd
      int patLength = (patIdxTmp - patIdxStart - 1);
      int strLength = (strIdxEnd - strIdxStart + 1);
      int foundIdx = -1;
      strLoop:
      for (int i = 0; i <= strLength - patLength; i++) {
        for (int j = 0; j < patLength; j++) {
          ch = patArr[patIdxStart + j + 1];
          if (ch != '?') {
            if (ch != strArr[strIdxStart + i + j]) {
              continue strLoop;
            }
          }
        }

        foundIdx = strIdxStart + i;
        break;
      }

      if (foundIdx == -1) {
        return false;
      }

      patIdxStart = patIdxTmp;
      strIdxStart = foundIdx + patLength;
    }

    // All characters in the string are used. Check if only '*'s are left
    // in the pattern. If so, we succeeded. Otherwise failure.
    for (int i = patIdxStart; i <= patIdxEnd; i++) {
      if (patArr[i] != '*') {
        return false;
      }
    }

    return true;
  }

  public String extractPathWithinPattern(String pattern, String path) {
    String[] patternParts = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator);
    String[] pathParts = StringUtils.tokenizeToStringArray(path, this.pathSeparator);

    StringBuilder buffer = new StringBuilder();

    // Add any path parts that have a wildcarded pattern part.
    int puts = 0;
    for (int i = 0; i < patternParts.length; i++) {
      String patternPart = patternParts[i];
      if ((patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) && pathParts.length >= i + 1) {
        if (puts > 0 || (i == 0 && !pattern.startsWith(this.pathSeparator))) {
          buffer.append(this.pathSeparator);
        }
        buffer.append(pathParts[i]);
        puts++;
      }
    }

    // Append any trailing path parts.
    for (int i = patternParts.length; i < pathParts.length; i++) {
      if (puts > 0 || i > 0) {
        buffer.append(this.pathSeparator);
      }
      buffer.append(pathParts[i]);
    }

    return buffer.toString();
  }

}

