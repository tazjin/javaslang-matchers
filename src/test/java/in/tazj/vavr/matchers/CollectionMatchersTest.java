package in.tazj.vavr.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import io.vavr.collection.List;

import static in.tazj.vavr.matchers.CollectionMatchers.allMatch;
import static in.tazj.vavr.matchers.CollectionMatchers.containsAny;
import static in.tazj.vavr.matchers.CollectionMatchers.containsElement;
import static in.tazj.vavr.matchers.CollectionMatchers.containsInAnyOrder;
import static in.tazj.vavr.matchers.CollectionMatchers.hasSize;
import static in.tazj.vavr.matchers.CollectionMatchers.isEmpty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class CollectionMatchersTest {
  @Test
  public void testIsEmpty() throws Exception {
    assertThat(List.empty(), isEmpty());
    assertThat(List.of(1), not(isEmpty()));
  }

  @Test
  public void testIsEmptyMismatch() {
    final Description description = new StringDescription();
    final String expected = "Collection was expected to be empty but has size <1>";
    isEmpty().describeMismatch(List.of(1), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasSize() throws Exception {
    assertThat(List.of(1, 2, 3, 4, 5), hasSize(5));
    assertThat(List.empty(), not(hasSize(5)));
  }

  @Test
  public void testHasSizeMismatch() {
    final Description description = new StringDescription();
    final String expected = "Collection should have size <1> but actually has size <2>";
    hasSize(1).describeMismatch(List.of(1, 2), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testHasSizeMatcher() throws Exception {
    assertThat(List.of(1, 2, 3), hasSize(lessThan(20)));
    assertThat(List.of(1, 2, 3), not(hasSize(lessThan(2))));
  }

  @Test
  public void testHasSizeMatcherMismatch() {
    final Description description = new StringDescription();
    final String expected = "Collection size does not match a value less than <2>, size was <3>";
    hasSize(lessThan(2)).describeMismatch(List.of(1, 2, 3), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testContainsElement() throws Exception {
    assertThat(List.of(1, 2, 3), containsElement(2));
    assertThat(List.of(1, 2, 3), not(containsElement(4)));
    assertThat(List.empty(), not(containsElement(1)));
  }

  @Test
  public void testContainsAny() throws Exception {
    assertThat(List.of(1, 2, 3), containsAny(is(1)));
    assertThat(List.of(2, 3, 4), not(containsAny(is(1))));
    assertThat(List.empty(), not(containsAny(is(1))));
  }

  @Test
  public void testContainsAnyMismatch() {
    final Description description = new StringDescription();
    final String expected =
        "Collection expected to contain a value matching 'is <5>' but found <List(1, 2)>";
    containsAny(is(5)).describeMismatch(List.of(1, 2), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testContainsInAnyOrder() throws Exception {
    assertThat(List.of(3, 2, 1), containsInAnyOrder(List.of(1, 2, 3)));
    assertThat(List.empty(), not(containsInAnyOrder(List.of(1, 2, 3))));
  }

  @Test
  public void testContainsInAnyOrderMismatch() {
    final Description description = new StringDescription();
    final String expected = "Collection is missing elements: [<4>,<5>,<6>]";
    containsInAnyOrder(List.of(1, 2, 3, 4, 5, 6)).describeMismatch(List.of(1, 2, 3), description);
    assertThat(description.toString(), is(expected));
  }

  @Test
  public void testAllMatch() {
    assertThat(List.of(1, 2, 3), allMatch(lessThan(5)));
    assertThat(List.of(1, 2, 6), not(allMatch(lessThan(5))));
  }

  @Test
  public void testAllMatchMismatch() {
    final Description description = new StringDescription();
    final String expected =
        "All elements should match 'is <true>' but found non-matching elements: [<false>,<false>]";
    allMatch(is(true)).describeMismatch(List.of(false, true, false), description);
    assertThat(description.toString(), is(expected));
  }
}
