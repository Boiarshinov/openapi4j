package org.openapi4j.operation.validator.adapters.server.okhttp3;

import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.operation.validator.model.Request;
import org.openapi4j.operation.validator.model.Response;
import org.openapi4j.operation.validator.validation.OperationValidator;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.schema.validator.ValidationData;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// not intended to be in the openapi4j code
// Just experiments
public class OpenApiValidator {

  private final Map<Pattern, String> pathByRegex = new HashMap<>();
  private final Map<OperationLocation, OperationValidator> validators = new HashMap<>();

  public OpenApiValidator(String specUrl) {
    try {
      URL fileUrl = OpenApiValidator.class.getResource(specUrl);
      OpenApi3 apiSpec = new OpenApi3Parser().parse(fileUrl, false);

      apiSpec.getPaths().forEach((templatedPath, ignored) -> {
        Pattern regex = PathToPatternMapper.map(templatedPath);
        pathByRegex.put(regex, templatedPath);
      });

      apiSpec.getPaths().forEach((templatedPath, path) -> path.getOperations()
        .forEach((method, operation) -> {
          OperationLocation operationLocation = new OperationLocation(templatedPath, method.toLowerCase());
          OperationValidator operationValidator = new OperationValidator(apiSpec, path, operation);
          validators.put(operationLocation, operationValidator);
        }));

    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid spec " + specUrl, e);
    }
  }

  public ValidationResults validate(Request request, Response response) {
    String path = request.getPath();
    Request.Method method = request.getMethod();

    OperationValidator validator = getValidator(path, method.name().toLowerCase());
    if (validator == null) {
      throw new RuntimeException("Validator not found for " + method + " " + path);
    }

    ValidationData<Object> validationData = new ValidationData<>();
    validator.validateResponse(response, validationData);
    return validationData.results();
  }

  private OperationValidator getValidator(String path, String method) {
    return pathByRegex.keySet().stream()
      .filter(pattern -> pattern.matcher(path).find())
      .findFirst()
      .map(pattern -> pathByRegex.get(pattern))
      .map(templatedPath -> new OperationLocation(templatedPath, method))
      .map(operationLocation -> validators.get(operationLocation))
      .orElse(null);
  }

  private static class OperationLocation {
    public String templatedPath;
    public String method;

    public OperationLocation(String templatedPath, String method) {
      this.templatedPath = templatedPath;
      this.method = method;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      OperationLocation that = (OperationLocation) o;

      if (!templatedPath.equals(that.templatedPath)) return false;
      return method.equals(that.method);
    }

    @Override
    public int hashCode() {
      int result = templatedPath.hashCode();
      result = 31 * result + method.hashCode();
      return result;
    }
  }
}
