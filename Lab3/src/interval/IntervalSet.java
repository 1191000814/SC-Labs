package Lab3.src.interval;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A mutable set of labeled intervals, where each unique label is associated with a non-overlapping
 * half-open interval [start,end). Labels are of immutable type L and must implement the Object contract:
 * they are compared for equality using Object.equals(java.lang.Object).
 * <p>
 * For example, { "A"=[0,10), "B"=[20,30) } is an interval set where the labels are Strings "A" and "B".
 * We could add "C"=[10,20) to such a set, but not "D"=[25,35) since that interval overlaps with "B"=[20,30).
 * <p>
 * PS2 instructions: this is a required ADT interface. You may not change the specifications or add new methods.
 *
 * @param <L> type of labels in this set, must be immutable
 */
public interface IntervalSet<L>{
    /**
     * Create an empty interval set.
     *
     * @param <L> type of labels of the interval set, must be immutable
     * @return a new empty interval set
     */
    public static <L> IntervalSet<L> empty(){
        return new CommonIntervalSet<>();
    }

    /**
     * Add a labeled interval (if not present) to this set, if it does not conflict
     * with existing intervals.
     * <p>
     * Labeled intervals conflict if: they have the same label with different
     * intervals, or they have different labels with overlapping intervals.
     * <p>
     * For example, if this set is { "A"=[0,10), "B"=[20,30) },
     * insert("A"=[0,10)) has no effect
     * insert("B"=[10,20)) throws IntervalConflictException
     * insert("C"=[20,30)) throws IntervalConflictException
     * insert("D"=[30,40)) adds "D"=[30,40)
     *
     *
     * @param interval the interval to insert
     * @throws IntervalConflictException if label is already in this set and its
     *                                   interval is not [start,end), or if an
     *                                   interval in this set with a different label
     *                                   overlaps [start,end)
     */
    public void insert(Interval<L> interval) throws IntervalConflictException, LabelRepeatException;

    /**
     * Get the labels in this set.
     *
     * @return the labels in this set
     */
    public Set<L> labels();

    /**
     * Remove a labeled interval from this set, if present.
     *
     * @param label to remove
     * @return true if this set contained label, and false otherwise
     */
    public boolean remove(L label);

    /**
     * Get the interval according to the label.
     *
     * @param label the label
     * @return interval, inclusive, of the interval associated with label
     * @throws NoSuchElementException - if label is not in this set
     */
    public List<Interval<L>> getIntervals(L label) throws NoSuchElementException;
}