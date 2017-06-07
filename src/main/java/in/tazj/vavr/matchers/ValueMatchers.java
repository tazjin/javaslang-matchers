package in.tazj.vavr.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.vavr.Value;

/**
 * Provides Hamcret matchers that can be used with any Vavr value type.
 */
public class ValueMatchers {
  /**
   * Matches any empty (undefined) Vavr {@link Value}.
   */
  public static <T extends Value> Matcher<T> isEmpty() {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        return t.isEmpty();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Value should be empty");
      }
    };
  }
}
