package net.footballpredictions.footballstats.util;

import org.testng.annotations.Test;
import java.util.SortedSet;
import java.util.Iterator;

/**
 * Unit test for the {@link FixedSizeSortedSet} class.
 * @author Daniel Dyer
 */
public class FixedSizeSortedSetTest
{
    /**
     * Make sure that the set behaves normally when it has not reached its
     * maximum size.
     */
    @Test
    public void testSmallerThanMaximumSize()
    {
        SortedSet<String> set = new FixedSizeSortedSet<String>(3);
        assert set.isEmpty() : "Set should be empty initially.";
        set.add("AAA");
        assert set.size() == 1 : "Set size should be 1, is " + set.size();
        // Adding further elements should increase the size of the set.
        set.add("BBB");
        set.add("CCC");
        assert set.size() == 3 : "Set size should be 3, is " + set.size();
    }


    @Test
    public void testAddDuplicate()
    {
        SortedSet<String> set = new FixedSizeSortedSet<String>(3);
        set.add("AAA");
        assert set.size() == 1 : "Set size should be 1, is " + set.size();
        // Adding a duplicate should not increase the size of the set.
        set.add("AAA");
        assert set.size() == 1 : "Set size should be 1, is " + set.size();
    }


    @Test
    public void testIterationOrder()
    {
        SortedSet<String> set = new FixedSizeSortedSet<String>(3);
        set.add("AAA");
        set.add("CCC"); // Insert out-of-order.
        set.add("BBB");
        // Iteration order should respect natural ordering of elements.
        Iterator<String> iterator = set.iterator();
        assert iterator.next().equals("AAA") : "Wrong first element.";
        assert iterator.next().equals("BBB") : "Wrong second element.";
        assert iterator.next().equals("CCC") : "Wrong third element.";
    }


    /**
     * Adding a value that is out-of-range into a full set should just
     * discard the added value.
     */
    @Test
    public void testInsertOutOfRangeValueIntoFullSet()
    {
        SortedSet<String> set = new FixedSizeSortedSet<String>(3);
        set.add("AAA");
        set.add("BBB");
        set.add("CCC");
        assert set.size() == 3 : "Set size should be 3, is " + set.size();
        // Next value is out-of-range.
        set.add("DDD");
        assert set.size() == 3 : "Set size should be 3, is " + set.size();
        assert !set.contains("DDD") : "Set should not contain out-of-range value.";
    }


    @Test
    public void testInsertIntermediateValueIntoFullSet()
    {
        SortedSet<String> set = new FixedSizeSortedSet<String>(3);
        set.add("AAA");
        set.add("BBB");
        set.add("DDD");
        assert set.size() == 3 : "Set size should be 3, is " + set.size();
        assert set.contains("DDD") : "Set should contain DDD.";
        // Next value is in range, last value should be discarded.
        set.add("CCC");
        assert set.size() == 3 : "Set size should be 3, is " + set.size();
        assert set.contains("CCC") : "Set should contain CCC.";
        assert !set.contains("DDD") : "Set should not contain last value.";
    }
}
