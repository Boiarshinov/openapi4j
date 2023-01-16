package org.openapi4j.operation.validator.adapters.server.okhttp3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.operation.validator.model.Response;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultResponse;
import org.openapi4j.operation.validator.validation.OperationValidator;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.parser.model.v3.Path;
import org.openapi4j.schema.validator.ValidationData;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteMe {

  @Test
  void test() throws Exception {
    OperationValidator validator = loadOperationValidator("/openapi_test.yaml", "refundRequestStatus");
    Response response = mockResponse("{\n" +
      "  \"code\": \"RQ00000\"" +
      ", \n" +
      "  \"message\": \"message\",\n" +
      "  \"data\": {\n" +
      "    \"agentRefundRequestId\": \"3842ca282aea11e88feca860b60304d\",\n" +
      "    \"status\": \"HEHE\",\n" +
      "    \"code\": \"RQ00030\",\n" +
      "    \"message\": \"message\"\n" +
      "  }\n" +
      "}");

    ValidationData<Object> validationData = new ValidationData<>();
    validator.validateResponse(response, validationData);

    System.out.println(validationData.results());
    assertTrue(validationData.isValid());
  }

  @Test
  void kek() throws ResolutionException, ValidationException {
    URL specPath = DeleteMe.class.getResource("/openapi_test.yaml");
    OpenApi3 api = new OpenApi3Parser().parse(specPath, false);

    Map<String, Path> paths = api.getPaths();
    System.out.println(paths);
  }

  private OperationValidator loadOperationValidator(String path, String opId) throws Exception {
    URL specPath = DeleteMe.class.getResource(path);
    OpenApi3 api = new OpenApi3Parser().parse(specPath, false);

    return new OperationValidator(
      api,
      api.getPathItemByOperationId(opId),
      api.getOperationById(opId));
  }

  private Response mockResponse(String jsonBody) {
    return new DefaultResponse.Builder(200)
      .header("Content-Type", "application/json")
      .body(Body.from(jsonBody))
      .build();
  }
}
