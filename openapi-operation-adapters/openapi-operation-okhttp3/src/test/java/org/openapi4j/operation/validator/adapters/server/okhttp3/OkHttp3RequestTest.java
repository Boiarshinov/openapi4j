package org.openapi4j.operation.validator.adapters.server.okhttp3;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.jupiter.api.Test;
import org.openapi4j.operation.validator.model.Request;
import org.openapi4j.operation.validator.model.impl.Body;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OkHttp3RequestTest {

  @Test
  void get() throws IOException {
    okhttp3.Request okHttpRequest = createOkHttpRequest("get");

    Request request = OkHttp3Request.of(okHttpRequest);

    assertEquals(Request.Method.GET, request.getMethod());
  }

  @Test
  void post() {

  }

  @Test
  void withBody() throws IOException {
    okhttp3.Request okHttpRequest = createOkHttpRequest("get", "{\"field\":  \"value\"}");

    Request request = OkHttp3Request.of(okHttpRequest);

    assertBodyEquals("{\"field\":  \"value\"}", request.getBody());
  }

  @Test
  void withUrlParams() throws IOException {
    okhttp3.Request okHttpRequest = createOkHttpRequest("https://domain.com/path?param=value", "get", null);

    Request request = OkHttp3Request.of(okHttpRequest);

    assertEquals("param=value", request.getQuery());
  }

  @Test
  void withHeaders() throws IOException {
    okhttp3.Request okHttpRequest = createOkHttpRequest("get");

    Request request = OkHttp3Request.of(okHttpRequest);

    assertEquals(new HashSet<>(Arrays.asList("x-header1", "x-header2")), request.getHeaders().keySet());
  }

  private okhttp3.Request createOkHttpRequest(String method) {
    return createOkHttpRequest(method, null);
  }

  private okhttp3.Request createOkHttpRequest(String method, String body) {
    return createOkHttpRequest("https://domain.com/url", method, body);
  }

  private okhttp3.Request createOkHttpRequest(String uri, String method, String body) {
    RequestBody requestBody = body == null
      ? null
      : RequestBody.create(body, MediaType.get("application/json"));
    return new okhttp3.Request.Builder()
      .url(uri)
      .method(method, requestBody)
      .addHeader("x-header1", "value1")
      .addHeader("x-header2", "value2")
      .build();
  }

  private void assertBodyEquals(String expected, Body actualBody) throws IOException {
    assertEquals(
      JsonNodeFactory.instance.textNode(expected),
      actualBody.getContentAsNode(null, null, null));
  }

}
