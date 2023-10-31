package fr.utbm.ai51.holonicsearch;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class SearchRequest extends Event {
  public final String predicate;

  public final String path;

  public SearchRequest(final String pa, final String pr) {
    this.path = pa;
    this.predicate = pr;
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SearchRequest other = (SearchRequest) obj;
    if (!Objects.equals(this.predicate, other.predicate))
      return false;
    if (!Objects.equals(this.path, other.path))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.predicate);
    result = prime * result + Objects.hashCode(this.path);
    return result;
  }

  /**
   * Returns a String representation of the SearchRequest event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("predicate", this.predicate);
    builder.add("path", this.path);
  }

  @SyntheticMember
  private static final long serialVersionUID = 1920764233L;
}
