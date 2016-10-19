package in.tazj.javaslang.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javaslang.collection.Traversable;

/**
 * Provides Hamcrest matchers for Javaslang collection types.
 * */
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
   * Matches Javaslang {@link Traversable} that contains a certain element.
   *
   * @param element The expected element.
   */
  public static <E, T extends Traversable<E>> Matcher<T> containsElement(E element) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T es) {
        return es.contains(element);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Collection should contain ").appendValue(element);
      }
    };
  }

  /**
   * Matches Javaslang {@link Traversable} that contains expected elements in any order.
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
        description.appendText("Collection should contain: ")
            .appendValueList("[", ",", "]", items);
      }

      @Override
      public void describeMismatchSafely(T t, Description mismatch) {
        final Iterable<E> missing = items.partition(t::contains)._2();
        mismatch.appendText("Collection is missing elements: ")
            .appendValueList("[", ",", "]", missing);
      }
    };
  }
}
