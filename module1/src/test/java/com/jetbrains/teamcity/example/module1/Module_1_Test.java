package com.jetbrains.teamcity.example.module1;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class Module_1_Test
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Module_1_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Module_1_Test.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void test1()
    {
        assertTrue( true );
    }

    public void test2()
    {
        assertTrue( true );
    }
}