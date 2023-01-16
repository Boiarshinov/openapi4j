package org.openapi4j.operation.validator.adapters.server.okhttp3;

import okhttp3.ResponseBody;
import org.openapi4j.operation.validator.model.Response;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultResponse;

import java.io.IOException;
import java.util.HashMap;

public abstract class OkHttp3Response implements Response {

  private OkHttp3Response() {
  }

  public static Response of(okhttp3.Response response) throws IOException {
    DefaultResponse.Builder builder = new DefaultResponse.Builder(response.code())
      .headers(new HashMap<>(response.headers().toMultimap()));
    if (response.body() != null) {
      ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
      builder.body(Body.from(responseBody.string()));
    }
    return builder.build();
  }
}
