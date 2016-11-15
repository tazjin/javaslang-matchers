package in.tazj.javaslang.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.control.Try;

import static in.tazj.javaslang.matchers.ControlMatchers.hasFailedWith;
import static in.tazj.javaslang.matchers.ControlMatchers.isDefined;
import static in.tazj.javaslang.matchers.ControlMatchers.isEmpty;
import static in.tazj.javaslang.matchers.ControlMatchers.isFailure;
import static in.tazj.javaslang.matchers.ControlMatchers.isLeft;
import static in.tazj.javaslang.matchers.ControlMatchers.isRight;
import static in.tazj.javaslang.matchers.ControlMatchers.isSuccess;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ControlMatchersTest {
  @Test
  public void testIsDefined() throws Exception {
    assertThat(Option.of(1), isDefined());
    assertThat(Option.none(), not(isDefined()));
  }

  @Test
  public void testIsDefinedSimpleMismatch() {
    final Description description = new StringDescription();
    isDefined().describeMismatch(Option.none(), description);
    assertThat(description.toString(), is("No value was defined"));
  }

  @Test
  public void testIsDefinedAdvancedMismatch() {
    final Description description = new StringDescription();
    final String expected = "<42> was greater than <20>";
    isDefined(lessThan(20)).describeMismatch(Option.of(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsEmpty() throws Exception {
    assertThat(Option.none(), isEmpty());
    assertThat(Option.of(1), not(isEmpty()));
  }

  @Test
  public void testIsEmptyMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected empty Option but found <42>";
    isEmpty().describeMismatch(Option.of(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsSuccess() throws Exception {
    assertThat(Try.success(1), isSuccess());
    assertThat(Try.of(() -> 1), isSuccess());

    assertThat(Try.failure(new Exception()), not(isSuccess()));
    assertThat(Try.of(() -> {
      throw new Exception();
    }), not(isSuccess()));
  }

  @Test
  public void testIsSuccessMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected success but got <java.lang.Exception>";
    isSuccess().describeMismatch(Try.failure(new Exception()), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsSuccessMatcher() throws Exception {
    assertThat(Try.success("fnord"), isSuccess(not(emptyString())));
    assertThat(Try.failure(new Exception()), not(isSuccess(anything())));
  }

  @Test
  public void testIsSuccessMatcherMismatch() {
    final Description description = new StringDescription();
    final String expected =
        "Expected successful Try value matching 'a value less than <20>' but <42> was greater than <20>";
    isSuccess(lessThan(20)).describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsFailure() throws Exception {
    assertThat(Try.failure(new Exception()), isFailure());
    assertThat(Try.of(() -> {
      throw new Exception();
    }), isFailure());

    assertThat(Try.success(1), not(isFailure()));
    assertThat(Try.of(() -> 1), not(isFailure()));
  }

  @Test
  public void testIsFailureMismatch() {
    final Description description = new StringDescription();
    final String expected = "Try should not have succeeded, but was <Success(42)>";
    isFailure().describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasFailedWith() throws Exception {
    assertThat(Try.failure(new CustomException()), hasFailedWith(CustomException.class));
    assertThat(Try.of(() -> {
      throw new CustomException();
    }), hasFailedWith(CustomException.class));

    assertThat(Try.failure(new Exception()), not(hasFailedWith(CustomException.class)));
    assertThat(Try.of(() -> {
      throw new Exception();
    }), not(hasFailedWith(CustomException.class)));

    assertThat(Try.success(1), not(hasFailedWith(CustomException.class)));
    assertThat(Try.of(() -> 1), not(hasFailedWith(CustomException.class)));
  }

  @Test
  public void testHasFailedWithMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected failure, but found successful Try with value: <42>";
    hasFailedWith(CustomException.class).describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasFailedWithExceptionMismatch() {
    final Description description = new StringDescription();
    final String expected = "Failure type is UnsupportedEncodingException but expected CustomException";
    hasFailedWith(CustomException.class)
        .describeMismatch(Try.of(() -> {
          throw new UnsupportedEncodingException();
        }), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsRight() throws Exception {
    assertThat(Either.right(1), isRight());
    assertThat(Either.left(1), not(isRight()));
  }

  @Test
  public void testIsRightMismatchLeft() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Right« value, but got »Left«: <42>";
    isRight().describeMismatch(Either.left(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsRightMismatchValue() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Right« value, but got: was <42>";
    isRight(is(1)).describeMismatch(Either.right(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsLeft() throws Exception {
    assertThat(Either.left(1), isLeft());
    assertThat(Either.right(1), not(isLeft()));
  }

  @Test
  public void testIsLeftMismatchRight() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Left« value, but got »Right«: <42>";
    isLeft().describeMismatch(Either.right(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsLeftMismatchValue() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Left« value, but got: was <42>";
    isLeft(is(1)).describeMismatch(Either.left(42), description);
    assertThat(description.toString(), is(expected));
  }

  static class CustomException extends Exception {
  }
}
