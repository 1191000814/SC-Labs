/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package Lab2.test.P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;

import Lab2.src.P1.graph.Graph;
import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest{
    
    // Testing strategy
    // 测试creators, producers, 和mutators: 调用observers来观察这些operations的结果是否满足spec
    // 测试observers: 调用creators, producers,和mutators等方法产生或改变对象,看结果是否正确
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        // 该抽象方法已经被其他类里的实例方法覆盖了,所以可以直接测试
        assertEquals(Collections.emptySet(), emptyInstance().vertices());
    }

    // TODO other tests for instance methods of Graph
    
}