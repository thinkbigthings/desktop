package org.thinkbigthings.desktop.controller;

import org.junitpioneer.jupiter.IssueProcessor;
import org.junitpioneer.jupiter.IssueTestSuite;

import java.util.List;

public class SimpleProcessor implements IssueProcessor {

    @Override
    public void processTestResults(List<IssueTestSuite> allResults) {
        for(IssueTestSuite testSuite : allResults) {
            testSuite.tests().forEach(t -> t.testId());
            System.out.println(testSuite.issueId());
        }
    }

}