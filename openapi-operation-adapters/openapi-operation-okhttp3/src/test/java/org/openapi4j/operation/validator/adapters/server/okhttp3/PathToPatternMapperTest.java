package org.openapi4j.operation.validator.adapters.server.okhttp3;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PathToPatternMapperTest {

  @Test
  void withOneTemplate() {
    String templatedPath = "/api/v1/entity/{entityId}";
    String path = "/api/v1/entity/123456";

    Pattern pattern = PathToPatternMapper.map(templatedPath);
    boolean pathMatches = pattern.matcher(path).find();

    assertTrue(pathMatches);
  }

  @Test
  void withTwoTemplates() {
    String templatedPath = "/api/v1/entity/{entityId}/{subEntityId}";
    String path = "/api/v1/entity/123456/abcdef";

    Pattern pattern = PathToPatternMapper.map(templatedPath);
    boolean pathMatches = pattern.matcher(path).find();

    assertTrue(pathMatches);
  }

}
