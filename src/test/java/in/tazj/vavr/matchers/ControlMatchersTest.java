package in.tazj.vavr.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import static in.tazj.vavr.matchers.ControlMatchers.isDefined;
import static in.tazj.vavr.matchers.ControlMatchers.isLeft;
import static in.tazj.vavr.matchers.ControlMatchers.isRight;
import static in.tazj.vavr.matchers.ControlMatchers.isSuccess;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ControlMatchersTest {
  @Test
  public void testIsDefined() throws Exception {
    assertThat(Option.of(1), ControlMatchers.isDefined());
    assertThat(Option.none(), Matchers.not(ControlMatchers.isDefined()));
  }

  @Test
  public void testIsDefinedSimpleMismatch() {
    final Description description = new StringDescription();
    ControlMatchers.isDefined().describeMismatch(Option.none(), description);
    assertThat(description.toString(), is("No value was defined"));
  }

  @Test
  public void testIsDefinedAdvancedMismatch() {
    final Description description = new StringDescription();
    final String expected = "<42> was greater than <20>";
    ControlMatchers.isDefined(lessThan(20)).describeMismatch(Option.of(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsEmpty() throws Exception {
    assertThat(Option.none(), ControlMatchers.isEmpty());
    assertThat(Option.of(1), Matchers.not(ControlMatchers.isEmpty()));
  }

  @Test
  public void testIsEmptyMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected empty Option but found <42>";
    ControlMatchers.isEmpty().describeMismatch(Option.of(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsSuccess() throws Exception {
    assertThat(Try.success(1), ControlMatchers.isSuccess());
    assertThat(Try.of(() -> 1), ControlMatchers.isSuccess());

    assertThat(Try.failure(new Exception()), Matchers.not(ControlMatchers.isSuccess()));
    assertThat(Try.of(() -> {
      throw new Exception();
    }), Matchers.not(ControlMatchers.isSuccess()));
  }

  @Test
  public void testIsSuccessMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected success but got <java.lang.Exception>";
    ControlMatchers.isSuccess().describeMismatch(Try.failure(new Exception()), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsSuccessMatcher() throws Exception {
    assertThat(Try.success("fnord"), ControlMatchers.isSuccess(not(emptyString())));
    assertThat(Try.failure(new Exception()), Matchers.not(ControlMatchers.isSuccess(anything())));
  }

  @Test
  public void testIsSuccessMatcherMismatch() {
    final Description description = new StringDescription();
    final String expected =
        "Expected successful Try value matching 'a value less than <20>' but <42> was greater than <20>";
    ControlMatchers.isSuccess(lessThan(20)).describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsFailure() throws Exception {
    assertThat(Try.failure(new Exception()), ControlMatchers.isFailure());
    assertThat(Try.of(() -> {
      throw new Exception();
    }), ControlMatchers.isFailure());

    assertThat(Try.success(1), Matchers.not(ControlMatchers.isFailure()));
    assertThat(Try.of(() -> 1), Matchers.not(ControlMatchers.isFailure()));
  }

  @Test
  public void testIsFailureMismatch() {
    final Description description = new StringDescription();
    final String expected = "Try should not have succeeded, but was <Success(42)>";
    ControlMatchers.isFailure().describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasFailedWith() throws Exception {
    assertThat(Try.failure(new CustomException()), ControlMatchers.hasFailedWith(CustomException.class));
    assertThat(Try.of(() -> {
      throw new CustomException();
    }), ControlMatchers.hasFailedWith(CustomException.class));

    assertThat(Try.failure(new Exception()), Matchers.not(ControlMatchers.hasFailedWith(CustomException.class)));
    assertThat(Try.of(() -> {
      throw new Exception();
    }), Matchers.not(ControlMatchers.hasFailedWith(CustomException.class)));

    assertThat(Try.success(1), Matchers.not(ControlMatchers.hasFailedWith(CustomException.class)));
    assertThat(Try.of(() -> 1), Matchers.not(ControlMatchers.hasFailedWith(CustomException.class)));
  }

  @Test
  public void testHasFailedWithMismatch() {
    final Description description = new StringDescription();
    final String expected = "Expected failure, but found successful Try with value: <42>";
    ControlMatchers.hasFailedWith(CustomException.class).describeMismatch(Try.success(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasFailedWithExceptionMismatch() {
    final Description description = new StringDescription();
    final String expected = "Failure type is UnsupportedEncodingException but expected CustomException";
    ControlMatchers.hasFailedWith(CustomException.class)
        .describeMismatch(Try.of(() -> {
          throw new UnsupportedEncodingException();
        }), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsRight() throws Exception {
    assertThat(Either.right(1), ControlMatchers.isRight());
    assertThat(Either.left(1), Matchers.not(ControlMatchers.isRight()));
  }

  @Test
  public void testIsRightMismatchLeft() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Right« value, but got »Left«: <42>";
    ControlMatchers.isRight().describeMismatch(Either.left(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsRightMismatchValue() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Right« value, but got: was <42>";
    ControlMatchers.isRight(is(1)).describeMismatch(Either.right(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsLeft() throws Exception {
    assertThat(Either.left(1), ControlMatchers.isLeft());
    assertThat(Either.right(1), Matchers.not(ControlMatchers.isLeft()));
  }

  @Test
  public void testIsLeftMismatchRight() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Left« value, but got »Right«: <42>";
    ControlMatchers.isLeft().describeMismatch(Either.right(42), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testIsLeftMismatchValue() throws Exception {
    final Description description = new StringDescription();
    final String expected = "Expected matching »Left« value, but got: was <42>";
    ControlMatchers.isLeft(is(1)).describeMismatch(Either.left(42), description);
    assertThat(description.toString(), is(expected));
  }

  static class CustomException extends Exception {
  }
}
