package org.openapi4j.parser.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.openapi4j.core.model.OAIContext;
import org.openapi4j.core.model.reference.Reference;
import org.openapi4j.core.util.TreeUtil;

import static org.openapi4j.core.model.reference.Reference.ABS_REF_FIELD;

/**
 * Base class for Open API schema which can be represented as reference.
 */
public abstract class AbsRefOpenApiSchema<M extends OpenApiSchema<M>> extends AbsOpenApiSchema<M> {
  @JsonProperty("$ref")
  private String ref;
  @JsonProperty(value = ABS_REF_FIELD, access = JsonProperty.Access.WRITE_ONLY)
  private String canonicalRef;

  // $ref
  public String getRef() {
    return ref;
  }

  public boolean isRef() {
    return ref != null;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public void setCanonicalRef(String canonicalRef) {
    this.canonicalRef = canonicalRef;
  }

  public String getCanonicalRef() {
    return canonicalRef;
  }

  public Reference getReference(OAIContext context) {
    return context.getReferenceRegistry().getRef(canonicalRef != null ? canonicalRef : ref);
  }

  @SuppressWarnings("unchecked")
  public M copy(OAIContext context, boolean followRefs) {
    if (isRef()) {
      if (followRefs) {
        Reference reference = getReference(context);
        if (reference != null) {
          M copy = (M) TreeUtil.json.convertValue(reference.getContent(), getClass());
          return copy.copy(context, true);
        }
      } else {
        return copyReference(context);
      }
    }

    return copyContent(context, followRefs);
  }

  /**
   * Copy the reference object.
   *
   * @param context The current context.
   * @return The copied reference object.
   */
  protected abstract M copyReference(OAIContext context);

  /**
   * Copy the flat content of the current schema.
   *
   * @param context    The current context.
   * @param followRefs {@code true} for following references.
   * @return The copied model.
   */
  protected abstract M copyContent(OAIContext context, boolean followRefs);
}
