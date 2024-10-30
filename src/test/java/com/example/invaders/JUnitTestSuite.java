package com.example.invaders;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        enemyTestCase.class,
        playerTest.class,
        scoreTest.class
            })
public class JUnitTestSuite {
}
