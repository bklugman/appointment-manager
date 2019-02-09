package com.bklugman.appointment.manager.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RuleToJUnit5Bridge {

    public static void runTestWithRule(final Runnable testToRun, TestRule rule) throws Throwable {
        Statement statement = new Statement() {
            @Override
            public void evaluate() {
                testToRun.run();
            }
        };
        rule.apply(statement, Description.EMPTY).evaluate();
    }
}
