package in.tazj.javaslang.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javaslang.collection.Traversable;

public class CollectionMatchers {
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

  public static <T extends Traversable> Matcher<T> hasSize(int size) {
    return new TypeSafeMatcher<T>() {
      @Override
      protected boolean matchesSafely(T t) {
        return (t.size() == size);
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

  public static<E, T extends Traversable<E>> Matcher<T> contains(E element) {
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
