package ch.heig.sio.lab1;

import ch.heig.sio.lab1.tsp.TspConstructiveHeuristic;
import ch.heig.sio.lab1.tsp.TspData;
import ch.heig.sio.lab1.tsp.TspTour;
import org.opentest4j.AssertionFailedError;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestUtils {
  /**
   * Test case for a TSP problem. Use {@link #test} to validate a test case.
   *
   * @param data           TSP data
   * @param startCity      Starting city index
   * @param expectedTour   Expected tour
   * @param expectedLength Expected tour length
   */
  public record TestCase(TspData data, int startCity, int[] expectedTour, long expectedLength) {
  }

  /**
   * Test case for a TSP problem with only one city. Any heuristic should return the same tour.
   */
  public static final TestCase ONE_CITY = new TestCase(
      TspData.fromArray(new TspData.City[]{
          new TspData.City(0, 0),
      }),
      0,
      new int[]{0},
      0
  );

  /**
   * Test case for a TSP problem with two cities. Any heuristic should return the same tour.
   */
  public static final TestCase TWO_CITIES = new TestCase(
      TspData.fromArray(new TspData.City[]{
          new TspData.City(0, 0),
          new TspData.City(0, 1),
      }),
      0,
      new int[]{0, 1},
      2
  );

  /**
   * Test case for a TSP problem with three cities. Any heuristic should return the same tour.
   */
  public static final TestCase THREE_CITIES = new TestCase(
      TspData.fromArray(new TspData.City[]{
          new TspData.City(0, 0),
          new TspData.City(0, 3),
          new TspData.City(4, 0),
      }),
      1,
      new int[]{0, 1, 2},
      12
  );

  /**
   * Data from exercise 1. Factor 10 is used to avoid too much precision loss with integer arithmetic.
   */
  public static final TspData EXERCISE_1 = TspData.fromArray(new TspData.City[]{
      new TspData.City(10, 0),
      new TspData.City(70, 10),
      new TspData.City(50, 20),
      new TspData.City(110, 80),
      new TspData.City(90, 70),
      new TspData.City(80, 80),
      new TspData.City(40, 60),
      new TspData.City(20, 90),
      new TspData.City(10, 110),
      new TspData.City(0, 90),
  });

  /**
   * Expected tour for exercise 1 with nearest insertion heuristic, starting from city 7.
   */
  public static final TestCase EXERCISE_1_NI = new TestCase(
      EXERCISE_1,
      7,
      new int[]{0, 2, 1, 4, 3, 5, 6, 7, 8, 9},
      398
  );

  /**
   * Expected tour for exercise 1 with farthest insertion heuristic, starting from city 7.
   */
  public static final TestCase EXERCISE_1_FI = new TestCase(
      EXERCISE_1,
      7,
      new int[]{0, 2, 1, 4, 3, 5, 8, 9, 7, 6},
      403
  );

  /**
   * Tests all heuristics on a test case. Only use for small test cases where all heuristics should return
   * the same result.
   *
   * @param heuristics Array of heuristics to test
   * @param testCase  Test case to validate
   */
  public static void testAll(TspConstructiveHeuristic[] heuristics, TestCase testCase) {
    for (var heuristic : heuristics) {
      test(heuristic, testCase);
    }
  }

  /**
   * Validates a test case with a heuristic.
   *
   * @param heuristic Heuristic to test
   * @param testCase  Test case to validate
   */
  public static void test(TspConstructiveHeuristic heuristic, TestCase testCase) {
    var tour = heuristic.computeTour(testCase.data, testCase.startCity);
    assertTourEquivalent(tour, testCase.expectedTour, testCase.expectedLength);
  }

  /**
   * Asserts that a tour is equivalent to a given tour, i.e. with the same cities in the same order or in reverse order
   * and with the same length.
   *
   * @param tour           Tour to validate
   * @param equivalentTour Equivalent tour
   * @param expectedLength Expected tour length
   */
  public static void assertTourEquivalent(TspTour tour, int[] equivalentTour, long expectedLength) {
    int n = equivalentTour.length;
    int[] actualTour = tour.tour();

    assertEquals(equivalentTour.length, actualTour.length, "Array length mismatch");

    // In the actual tour, find the index of the first city of the equivalent tour
    int start = 0;
    for (int i = 0; i < n; i++) {
      if (actualTour[i] == equivalentTour[0]) {
        start = i;
        break;
      }
    }

    // A tour is equivalent if its cities are in the same order or in reverse order
    boolean inOrder = true;
    boolean reverseOrder = true;
    for (int i = 0; i < n; i++) {
      // Indexes in the actual tour, computed from the start index
      int orderIdx = (start + i) % n;
      int reverseIdx = (start - i + n) % n;

      if (actualTour[orderIdx] != equivalentTour[i])
        inOrder = false;

      if (actualTour[reverseIdx] != equivalentTour[i])
        reverseOrder = false;
    }

    if (!inOrder && !reverseOrder)
      throw new AssertionFailedError("Tour mismatch", Arrays.toString(equivalentTour), Arrays.toString(actualTour));

    assertEquals(expectedLength, tour.length(), "Tour length mismatch");
  }

  private TestUtils() {
    throw new AssertionError("Can't instantiate a utility class");
  }
}
