package com.micro.webcore.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil {

  private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

  public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
  public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
  public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
  public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
  public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

  public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

  public static String getPathWithinApplication(HttpServletRequest request) {
    String contextPath = getContextPath(request);
    String requestUri = getRequestUri(request);
    if (PathUtil.startsWithIgnoreCase(requestUri, contextPath)) {
      // Normal case: URI contains context path.
      String path = requestUri.substring(contextPath.length());
      return (PathUtil.hasText(path) ? path : "/");
    } else {
      // Special case: rather unusual.
      return requestUri;
    }
  }

  public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
      contextPath = request.getContextPath();
    }
    contextPath = normalize(decodeRequestString(request, contextPath));
    if ("/".equals(contextPath)) {
      // the normalize method will return a "/" and includes on Jetty, will also be a "/".
      contextPath = "";
    }
    return contextPath;
  }

  public static String getRequestUri(HttpServletRequest request) {
    String uri = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
    if (uri == null) {
      uri = request.getRequestURI();
    }
    return normalize(decodeAndCleanUriString(request, uri));
  }

  public static String normalize(String path) {
    return normalize(path, true);
  }

  private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
    uri = decodeRequestString(request, uri);
    int semicolonIndex = uri.indexOf(';');
    return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
  }

  public static String decodeRequestString(HttpServletRequest request, String source) {
    String enc = determineEncoding(request);
    try {
      return URLDecoder.decode(source, enc);
    } catch (UnsupportedEncodingException ex) {
      if (log.isWarnEnabled()) {
        log.warn("Could not decode request string [" + source + "] with encoding '" + enc +
            "': falling back to platform default encoding; exception message: " + ex.getMessage());
      }
      return URLDecoder.decode(source);
    }
  }

  protected static String determineEncoding(HttpServletRequest request) {
    String enc = request.getCharacterEncoding();
    if (enc == null) {
      enc = DEFAULT_CHARACTER_ENCODING;
    }
    return enc;
  }


  private static String normalize(String path, boolean replaceBackSlash) {

    if (path == null) {
      return null;
    }

    // Create a place for the normalized path
    String normalized = path;

    if (replaceBackSlash && normalized.indexOf('\\') >= 0) {
      normalized = normalized.replace('\\', '/');
    }

    if (normalized.equals("/.")) {
      return "/";
    }

    // Add a leading "/" if necessary
    if (!normalized.startsWith("/")) {
      normalized = "/" + normalized;
    }

    // Resolve occurrences of "//" in the normalized path
    while (true) {
      int index = normalized.indexOf("//");
      if (index < 0) {
        break;
      }
      normalized = normalized.substring(0, index) +
          normalized.substring(index + 1);
    }

    // Resolve occurrences of "/./" in the normalized path
    while (true) {
      int index = normalized.indexOf("/./");
      if (index < 0) {
        break;
      }
      normalized = normalized.substring(0, index) +
          normalized.substring(index + 2);
    }

    // Resolve occurrences of "/../" in the normalized path
    while (true) {
      int index = normalized.indexOf("/../");
      if (index < 0) {
        break;
      }
      if (index == 0) {
        return (null);  // Trying to go outside our context
      }
      int index2 = normalized.lastIndexOf('/', index - 1);
      normalized = normalized.substring(0, index2) +
          normalized.substring(index + 3);
    }

    // Return the normalized path that we have completed
    return (normalized);

  }


}
