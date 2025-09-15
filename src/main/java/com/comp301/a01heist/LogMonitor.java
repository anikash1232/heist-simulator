package com.comp301.a01heist;

public class LogMonitor {
  private int failedAttempts = 0;
  private static final int MAX_FAILURES = 5;

  public void recordFailure() {
    failedAttempts++;
  }

  public boolean isLocked() {
    return failedAttempts >= MAX_FAILURES;
  }
}
