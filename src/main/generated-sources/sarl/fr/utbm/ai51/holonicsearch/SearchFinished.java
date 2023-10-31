package fr.utbm.ai51.holonicsearch;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;

@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class SearchFinished extends Event {
  @SyntheticMember
  public SearchFinished() {
    super();
  }

  @SyntheticMember
  public SearchFinished(final Address source) {
    super(source);
  }

  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
