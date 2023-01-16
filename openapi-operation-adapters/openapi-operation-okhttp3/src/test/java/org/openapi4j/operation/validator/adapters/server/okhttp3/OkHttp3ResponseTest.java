package org.openapi4j.operation.validator.adapters.server.okhttp3;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.openapi4j.operation.validator.model.Response;
import org.openapi4j.operation.validator.model.impl.Body;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class OkHttp3ResponseTest {

  @Test
  void bodyAndHeaders() throws IOException {
    okhttp3.Response okHttpResponse = createOkHttpResponse("{\"field\":  \"value\"}");

    Response response = OkHttp3Response.of(okHttpResponse);

    assertEquals(200, response.getStatus());
    assertBodyEquals("{\"field\":  \"value\"}", response.getBody());
    assertEquals("application/json", response.getContentType());
    assertEquals(new HashSet<>(Arrays.asList("content-type", "date")), response.getHeaders().keySet());
  }

  @Test
  void withoutBody() throws IOException {
    okhttp3.Response okHttpResponse = createOkHttpResponse(null);

    Response response = OkHttp3Response.of(okHttpResponse);

    assertEquals(200, response.getStatus());
    assertNull(response.getBody());
    assertEquals(new HashSet<>(Arrays.asList("content-type", "date")), response.getHeaders().keySet());
  }

  private okhttp3.Request createOkhttpRequest() {
    return new Request.Builder()
      .url("https://domain.com/url")
      .method("get", null)
      .build();
  }

  private okhttp3.Response createOkHttpResponse(String body) {
    okhttp3.Response.Builder builder = new okhttp3.Response.Builder()
      .request(createOkhttpRequest())
      .protocol(Protocol.HTTP_1_1)
      .code(200)
      .message("OK")
      .headers(Headers.of(
        "Content-Type", "application/json",
        "Date", "Sat, 24 Feb 2022 03:00:00 GMT"
      ));

    if (body != null) {
      builder.body(ResponseBody.create(body, MediaType.get("application/json")));
    }
    return builder.build();
  }

  private void assertBodyEquals(String expected, Body actualBody) throws IOException {
    assertEquals(
      JsonNodeFactory.instance.textNode(expected),
      actualBody.getContentAsNode(null, null, null));
  }

}
