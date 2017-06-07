package in.tazj.vavr.matchers;

import org.junit.Test;

import io.vavr.collection.List;

import static in.tazj.vavr.matchers.ValueMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Test the matchers in a real-world test.
 */
public class ValueMatchersTest {
  @Test
  public void testIsEmpty() {
    final List emptyList = List.of();
    assertThat(emptyList, isEmpty());

    final List nonEmptyList = List.of(1);
    assertThat(nonEmptyList, not(isEmpty()));
  }
}
