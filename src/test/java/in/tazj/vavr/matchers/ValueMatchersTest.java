package in.tazj.vavr.matchers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import io.vavr.collection.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Test the matchers in a real-world test.
 */
public class ValueMatchersTest {
  @Test
  public void testIsEmpty() {
    final List emptyList = List.of();
    MatcherAssert.assertThat(emptyList, ValueMatchers.isEmpty());

    final List nonEmptyList = List.of(1);
    MatcherAssert.assertThat(nonEmptyList, Matchers.not(ValueMatchers.isEmpty()));
  }
}
