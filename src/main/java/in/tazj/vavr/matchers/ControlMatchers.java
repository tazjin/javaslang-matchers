package in.tazj.vavr.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsAnything;

import io.vavr.control.Either;
import io.vavr.control.Either.Left;
import io.vavr.control.Either.Right;
import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * Provides Hamcrest matchers for types from Vavr's control package.
 */
public class ControlMatchers {
  /**
   * Matches a Vavr {@link Option} that has a matching defined value.
   */
  public static <T> Matcher<Option<T>> isDefined(Matcher<T> matcher) {
    return new TypeSafeMatcher<Option<T>>() {
      @Override
      protected boolean matchesSafely(Option<T> ts) {
        return ts.map(matcher::matches).getOrElse(false);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendValue("Option that contains value matching ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(Option<T> option, Description mismatch) {
        if (option.isEmpty()) {
          mismatch.appendText("No value was defined");
        } else {
          option.forEach(value -> matcher.describeMismatch(value, mismatch));
        }
      }
    };
  }

  /**
   * Matches a Vavr {@link Option} that has any defined value.
   */
  public static <T> Matcher<Option<T>> isDefined() {
    return isDefined(new IsAnything<T>());
  }

  /**
   * Matches a Vavr {@link Option} that has no defined value.
   */
  public static Matcher<Option> isEmpty() {
    return new TypeSafeMatcher<Option>() {
      @Override
      protected boolean matchesSafely(Option option) {
        return option.isEmpty();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Optional value should not be defined");
      }

      @Override
      public void describeMismatchSafely(Option option, Description mismatch) {
        mismatch.appendText("Expected empty Option but found ").appendValue(option.get());
      }
    };
  }

  /**
   * Matches the value of a Vavr {@link Try} that succeeded.
   */
  public static <T> Matcher<Try<T>> isSuccess(Matcher<T> matcher) {
    return new TypeSafeMatcher<Try<T>>() {
      @Override
      protected boolean matchesSafely(Try<T> ts) {
        return ts.map(matcher::matches).getOrElse(false);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("Successful Try should contain value that matches: ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(Try<T> aTry, Description mismatch) {
        if (aTry.isFailure()) {
          mismatch.appendText("Expected success but got ").appendValue(aTry.getCause());
        } else {
          mismatch
              .appendText("Expected successful Try value matching '")
              .appendDescriptionOf(matcher)
              .appendText("' but ");

          matcher.describeMismatch(aTry.get(), mismatch);
        }
      }
    };
  }

  /**
   * Matches a Vavr {@link Try} that succeeded.
   */
  public static <T> Matcher<Try<T>> isSuccess() {
    return isSuccess(new IsAnything<T>());
  }

  /**
   * Matches a Vavr {@link Try} that failed.
   */
  public static Matcher<Try> isFailure() {
    return new TypeSafeMatcher<Try>() {
      @Override
      protected boolean matchesSafely(Try aTry) {
        return aTry.isFailure();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("unsuccessful Try");
      }

      public void describeMismatchSafely(Try aTry, Description mismatch) {
        mismatch
            .appendText("Try should not have succeeded, but was ")
            .appendValue(aTry);
      }
    };
  }

  /**
   * Matches a Vavr {@link Try} that failed with an expected exception type.
   *
   * @param clazz The expected exception type.
   */
  public static <E extends Throwable> Matcher<Try> hasFailedWith(Class<E> clazz) {
    return new TypeSafeMatcher<Try>() {
      @Override
      protected boolean matchesSafely(Try aTry) {
        if (aTry.isFailure()) {
          return (aTry.getCause().getClass().equals(clazz));
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Try should have failed with ").appendText(clazz.getName());
      }

      @Override
      public void describeMismatchSafely(Try aTry, Description mismatch) {
        aTry.onFailure(cause -> mismatch
            .appendText("Failure type is ")
            .appendText(cause.getClass().getSimpleName())
            .appendText(" but expected ")
            .appendText(clazz.getSimpleName()));
        aTry.onSuccess(val -> mismatch
            .appendText("Expected failure, but found successful Try with value: ")
            .appendValue(val));
      }
    };
  }

  /**
   * Matches the {@link Right} value of a Vavr {@link Either}.
   *
   * @param matcher Matcher for the right value.
   */
  public static <L, R> Matcher<Either<L, R>> isRight(Matcher<R> matcher) {
    return new TypeSafeMatcher<Either<L, R>>() {
      @Override
      protected boolean matchesSafely(Either<L, R> either) {
        return either.map(matcher::matches).getOrElse(false);
      }

      @Override
      public void describeTo(Description description) {
        description
            .appendText("»Either« should contain a »Right« value matching: ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatchSafely(Either<L, R> either, Description mismatch) {
        if (either.isRight()) {
          mismatch.appendText("Expected matching »Right« value, but got: ");
          matcher.describeMismatch(either.get(), mismatch);
        } else {
          mismatch
              .appendText("Expected matching »Right« value, but got »Left«: ")
              .appendValue(either.getLeft());
        }
      }
    };
  }

  /**
   * Matches a Vavr {@link Either} that contains any {@link Right} value.
   */
  public static <L, R> Matcher<Either<L, R>> isRight() {
    return isRight(new IsAnything<R>());
  }

  /**
   * Matches the {@link Left} value of a Vavr {@link Either}.
   *
   * @param matcher Matcher for the left value.
   */
  public static <L, R> Matcher<Either<L, R>> isLeft(Matcher<L> matcher) {
    return new TypeSafeMatcher<Either<L, R>>() {
      @Override
      protected boolean matchesSafely(Either<L, R> either) {
        return (either.isLeft() && either.mapLeft(matcher::matches).getLeft());
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("»Either« should contain a »Left« value matching: ");
        matcher.describeTo(description);
      }

      @Override
      public void describeMismatchSafely(Either<L, R> either, Description mismatch) {
        if (either.isLeft()) {
          mismatch.appendText("Expected matching »Left« value, but got: ");
          matcher.describeMismatch(either.getLeft(), mismatch);
        } else {
          mismatch
              .appendText("Expected matching »Left« value, but got »Right«: ")
              .appendValue(either.get());
        }
      }
    };
  }

  /**
   * Matches a Vavr {@link Either} that contains any {@link Left} value.
   */
  public static <L, R> Matcher<Either<L, R>> isLeft() {
    return isLeft(new IsAnything<L>());
  }
}
