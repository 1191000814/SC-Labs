/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package Lab2.test.P1.graph;

import java.util.Collections;

import Lab2.src.P1.graph.Graph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    // empty()
    // no inputs, only output is empty graph
    // observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        Assert.assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    @Test
    public void testEmpty(){
        Assert.assertEquals(Graph.empty().vertices(), new ConcreteEdgesGraphTest().vertices);
    }
}
