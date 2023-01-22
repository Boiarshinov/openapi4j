package org.openapi4j.operation.validator.adapters.server.okhttp3;

import java.util.regex.Pattern;

public class PathToPatternMapper {

  /**
   * Map endpoint path with params in curly brackets to regex pattern.
   * Example:
   * Path: {@code /api/v1/entity/{entityId}/{subEntityId}}
   * Pattern: {@code ^/api/v1/entity/[^/]+/[^/]+$}
   * @param path endpoint path
   * @return regex pattern
   */
  public static Pattern map(String path) {
    String templatesReplaced = path.replaceAll("\\{.+?}", "[^/]+");
    return Pattern.compile("^" + templatesReplaced + "$");
  }
}
