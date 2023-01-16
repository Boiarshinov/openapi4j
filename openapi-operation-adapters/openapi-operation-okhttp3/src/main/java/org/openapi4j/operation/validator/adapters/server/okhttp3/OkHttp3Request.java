package org.openapi4j.operation.validator.adapters.server.okhttp3;

import okio.Buffer;
import org.openapi4j.operation.validator.model.Request;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultRequest;

import java.io.IOException;
import java.util.HashMap;

public abstract class OkHttp3Request implements Request {

  private OkHttp3Request() {
  }

  public static Request of(okhttp3.Request request) throws IOException {
    DefaultRequest.Builder builder = new DefaultRequest.Builder(
      request.url().toString(),
      Method.getMethod(request.method()));
    builder.headers(new HashMap<>(request.headers().toMultimap()));
    builder.query(request.url().query());
    if (request.body() != null) {
      builder.body(Body.from(bodyToString(request)));
    }
    return builder.build();
  }

  private static String bodyToString(final okhttp3.Request request) throws IOException {
      final okhttp3.Request copy = request.newBuilder().build();
      final Buffer buffer = new Buffer();
      copy.body().writeTo(buffer);
      return buffer.readUtf8();
  }
}
