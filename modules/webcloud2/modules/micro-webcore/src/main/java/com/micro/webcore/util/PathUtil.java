package com.micro.webcore.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class PathUtil {

  public static final String EMPTY_STRING = "";

  public static final char DEFAULT_DELIMITER_CHAR = ',';

  public static final char DEFAULT_QUOTE_CHAR = '"';

  public static boolean hasText(String str) {
    if (!hasLength(str)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasLength(String str) {
    return (str != null && str.length() > 0);
  }

  public static boolean startsWithIgnoreCase(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    if (str.startsWith(prefix)) {
      return true;
    }
    if (str.length() < prefix.length()) {
      return false;
    }
    String lcStr = str.substring(0, prefix.length()).toLowerCase();
    String lcPrefix = prefix.toLowerCase();
    return lcStr.equals(lcPrefix);
  }

  public static String clean(String in) {
    String out = in;

    if (in != null) {
      out = in.trim();
      if (out.equals(EMPTY_STRING)) {
        out = null;
      }
    }

    return out;
  }

  public static String toString(Object[] array) {
    return toDelimitedString(array, ",");
  }

  public static String toDelimitedString(Object[] array, String delimiter) {
    if (array == null || array.length == 0) {
      return EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(delimiter);
      }
      sb.append(array[i]);
    }
    return sb.toString();
  }

  public static String toDelimitedString(Collection c, String delimiter) {
    if (c == null || c.isEmpty()) {
      return EMPTY_STRING;
    }
    return join(c.iterator(), delimiter);
  }

  public static String[] tokenizeToStringArray(String str, String delimiters) {
    return tokenizeToStringArray(str, delimiters, true, true);
  }

  @SuppressWarnings({"unchecked"})
  public static String[] tokenizeToStringArray(
      String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

    if (str == null) {
      return null;
    }
    StringTokenizer st = new StringTokenizer(str, delimiters);
    List tokens = new ArrayList();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (trimTokens) {
        token = token.trim();
      }
      if (!ignoreEmptyTokens || token.length() > 0) {
        tokens.add(token);
      }
    }
    return toStringArray(tokens);
  }

  @SuppressWarnings({"unchecked"})
  public static String[] toStringArray(Collection collection) {
    if (collection == null) {
      return null;
    }
    return (String[]) collection.toArray(new String[collection.size()]);
  }

  public static String[] splitKeyValue(String aLine) throws ParseException {
    String line = clean(aLine);
    if (line == null) {
      return null;
    }
    String[] split = line.split(" ", 2);
    if (split.length != 2) {
      //fallback to checking for an equals sign
      split = line.split("=", 2);
      if (split.length != 2) {
        String msg = "Unable to determine Key/Value pair from line [" + line + "].  There is no space from " +
            "which the split location could be determined.";
        throw new ParseException(msg, 0);
      }

    }

    split[0] = clean(split[0]);
    split[1] = clean(split[1]);
    if (split[1].startsWith("=")) {
      //they used spaces followed by an equals followed by zero or more spaces to split the key/value pair, so
      //remove the equals sign to result in only the key and values in the
      split[1] = clean(split[1].substring(1));
    }

    if (split[0] == null) {
      String msg = "No valid key could be found in line [" + line + "] to form a key/value pair.";
      throw new ParseException(msg, 0);
    }
    if (split[1] == null) {
      String msg = "No corresponding value could be found in line [" + line + "] for key [" + split[0] + "]";
      throw new ParseException(msg, 0);
    }

    return split;
  }

  public static String[] split(String line) {
    return split(line, DEFAULT_DELIMITER_CHAR);
  }

  public static String[] split(String line, char delimiter) {
    return split(line, delimiter, DEFAULT_QUOTE_CHAR);
  }

  public static String[] split(String line, char delimiter, char quoteChar) {
    return split(line, delimiter, quoteChar, quoteChar);
  }

  public static String[] split(String line, char delimiter, char beginQuoteChar, char endQuoteChar) {
    return split(line, delimiter, beginQuoteChar, endQuoteChar, false, true);
  }

  public static String[] split(String aLine, char delimiter, char beginQuoteChar, char endQuoteChar,
      boolean retainQuotes, boolean trimTokens) {
    String line = clean(aLine);
    if (line == null) {
      return null;
    }

    List<String> tokens = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < line.length(); i++) {

      char c = line.charAt(i);
      if (c == beginQuoteChar) {
        // this gets complex... the quote may end a quoted block, or escape another quote.
        // do a 1-char lookahead:
        if (inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
            && line.length() > (i + 1)  // there is indeed another character to check.
            && line.charAt(i + 1) == beginQuoteChar) { // ..and that char. is a quote also.
          // we have two quote chars in a row == one quote char, so consume them both and
          // put one on the token. we do *not* exit the quoted text.
          sb.append(line.charAt(i + 1));
          i++;
        } else {
          inQuotes = !inQuotes;
          if (retainQuotes) {
            sb.append(c);
          }
        }
      } else if (c == endQuoteChar) {
        inQuotes = !inQuotes;
        if (retainQuotes) {
          sb.append(c);
        }
      } else if (c == delimiter && !inQuotes) {
        String s = sb.toString();
        if (trimTokens) {
          s = s.trim();
        }
        tokens.add(s);
        sb = new StringBuilder(); // start work on next token
      } else {
        sb.append(c);
      }
    }
    String s = sb.toString();
    if (trimTokens) {
      s = s.trim();
    }
    tokens.add(s);
    return tokens.toArray(new String[tokens.size()]);
  }

  public static String join(Iterator<?> iterator, String separator) {
    final String empty = "";

    // handle null, zero and one elements before building a buffer
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return empty;
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      return first == null ? empty : first.toString();
    }

    // two or more elements
    StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      if (separator != null) {
        buf.append(separator);
      }
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }
    return buf.toString();
  }

  public static Set<String> splitToSet(String delimited, String separator) {
    if (delimited == null || separator == null) {
      return null;
    }
    String[] split = split(delimited, separator.charAt(0));
    return asSet(split);
  }

  public static String uppercaseFirstChar(String in) {
    if (in == null || in.length() == 0) {
      return in;
    }
    int length = in.length();
    StringBuilder sb = new StringBuilder(length);

    sb.append(Character.toUpperCase(in.charAt(0)));
    if (length > 1) {
      String remaining = in.substring(1);
      sb.append(remaining);
    }
    return sb.toString();
  }

  public static <E> Set<E> asSet(E... elements) {
    if (elements == null || elements.length == 0) {
      return Collections.emptySet();
    }

    if (elements.length == 1) {
      return Collections.singleton(elements[0]);
    }

    LinkedHashSet<E> set = new LinkedHashSet<E>(elements.length * 4 / 3 + 1);
    Collections.addAll(set, elements);
    return set;
  }
}
