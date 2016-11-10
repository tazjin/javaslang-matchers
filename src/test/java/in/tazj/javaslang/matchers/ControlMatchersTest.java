package in.tazj.javaslang.matchers;

import org.junit.Test;

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
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ControlMatchersTest {
  @Test
  public void testIsDefined() throws Exception {
    assertThat(Option.of(1), isDefined());
    assertThat(Option.none(), not(isDefined()));
  }

  @Test
  public void testIsEmpty() throws Exception {
    assertThat(Option.none(), isEmpty());
    assertThat(Option.of(1), not(isEmpty()));
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
  public void testIsSuccessMatcher() throws Exception {
    assertThat(Try.success("fnord"), isSuccess(not(emptyString())));
    assertThat(Try.failure(new Exception()), not(isSuccess(anything())));
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
  public void testIsRight() throws Exception {
    assertThat(Either.right(1), isRight());
    assertThat(Either.left(1), not(isRight()));
  }

  @Test
  public void testIsLeft() throws Exception {
    assertThat(Either.left(1), isLeft());
    assertThat(Either.right(1), not(isLeft()));
  }

  static class CustomException extends Exception {
  }

}
