package org.openapi4j.operation.validator.adapters.server.okhttp3;

import org.junit.jupiter.api.Test;
import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.operation.validator.model.Request;
import org.openapi4j.operation.validator.model.Response;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultRequest;
import org.openapi4j.operation.validator.model.impl.DefaultResponse;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiValidatorTest {

  @Test
  void test() {
    OpenApiValidator openApiValidator = new OpenApiValidator("/openapi_test.yaml");
    Request request = new DefaultRequest.Builder(
      "/payment/v1/agent/refund/123456/abcdefg",
      Request.Method.GET)
      .headers(new HashMap<>())
      .build();

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

    ValidationResults validationResults = openApiValidator.validate(request, response);
    assertFalse(validationResults.isValid());
    assertEquals(2, validationResults.size());
    //todo add asserts
  }

  private Response mockResponse(String jsonBody) {
    return new DefaultResponse.Builder(200)
      .header("Content-Type", "application/json")
      .body(Body.from(jsonBody))
      .build();
  }
}
