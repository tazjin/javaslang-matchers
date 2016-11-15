package in.tazj.javaslang.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javaslang.collection.Traversable;

import static org.hamcrest.Matchers.is;

/**
 * Provides Hamcrest matchers for Javaslang collection types.
 */
public class CollectionMatchers {
  /**
   * Matches empty Javaslang {@link Traversable}.
   */
  public static <T extends Traversable> Matcher<T> isEmpty() {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        return t.isEmpty();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Collection should be empty");
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        mismatch.appendText("Collection was expected to be empty but has size ")
            .appendValue(t.size());
      }
    };
  }

  /**
   * Matches Javaslang {@link Traversable} with a given size.
   *
   * @param size The expected size of the traversable.
   */
  public static <T extends Traversable> Matcher<T> hasSize(int size) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        if (t.hasDefiniteSize()) {
          return (t.size() == size);
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Collection should have size ").appendValue(size);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        mismatch.appendText("Collection should have size ")
            .appendValue(size)
            .appendText(" but actually has size ")
            .appendValue(t.size());
      }
    };
  }

  /**
   * Matches Javaslang {@link Traversable} with a size matching a provided matcher.
   *
   * @param matcher A Hamcrest matcher to match the expected size.
   */
  public static <T extends Traversable> Matcher<T> hasSize(Matcher<Integer> matcher) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        if (t.hasDefiniteSize()) {
          return matcher.matches(t.size());
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("Collection size should match: ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        mismatch
            .appendText("Collection size does not match ")
            .appendDescriptionOf(matcher)
            .appendText(", size was ")
            .appendValue(t.size());
      }
    };
  }

  /**
   * Matches a Javaslang {@link Traversable} that contains a certain element.
   *
   * This matcher is deprecated and you should use containsAny(is(element)) instead.
   *
   * @param element The expected element.
   */
  @Deprecated
  public static <E, T extends Traversable<E>> Matcher<T> containsElement(E element) {
    return containsAny(is(element));
  }

  /**
   * Matches a Javaslang {@link Traversable} that contains at least one element matching the
   * supplied matcher.
   *
   * @param matcher The element matcher.
   */
  public static <E, T extends Traversable<E>> Matcher<T> containsAny(Matcher<E> matcher) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        return t.find(matcher::matches).isDefined();
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("At least one element should match: ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        mismatch
            .appendText("Collection expected to contain a value matching '")
            .appendDescriptionOf(matcher)
            .appendText("' but found ")
            .appendValue(t);
      }
    };
  }

  /**
   * Matches a Javaslang {@link Traversable} that contains expected elements in any order.
   *
   * @param items The expected elements.
   */
  public static <E, T extends Traversable<E>> Matcher<T> containsInAnyOrder(Traversable<E> items) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T es) {
        return es.containsAll(items);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("Collection should contain: ")
            .appendValueList("[", ",", "]", items);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        final Iterable<E> missing = items.partition(t::contains)._2();
        mismatch
            .appendText("Collection is missing elements: ")
            .appendValueList("[", ",", "]", missing);
      }
    };
  }

  /**
   * Matches a Javaslang {@link Traversable} whose elements all match the supplied element matcher.
   *
   * @param matcher The element matcher.
   */
  public static <E, T extends Traversable<E>> Matcher<T> allMatch(Matcher<E> matcher) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        return t.forAll(matcher::matches);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("All elements should match: ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        mismatch
            .appendText("All elements should match '")
            .appendDescriptionOf(matcher)
            .appendText("' but found non-matching elements: ");

        mismatch.appendValueList("[", ",", "]", t.filter(e -> !matcher.matches(e)).toJavaList());
      }
    };
  }
}
