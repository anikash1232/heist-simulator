package com.comp301.a01heist;

public class Vault {
  private final String secret;
  private boolean isLocked = true;
  private final LogMonitor logMonitor = new LogMonitor();

  public Vault() {
    this("TOP_SECRET_LAUNCH_CODES");
  }

  public Vault(String secret) {
    this.secret = secret;
  }

  public boolean accessVault(
      SecurityLayer layer, AccessKey key, String passAttempt, String bioAttempt) {
    if (logMonitor.isLocked()) {
      return false;
    }

    boolean authed = layer.authenticate(passAttempt, bioAttempt);
    if (authed && key.isValid()) {
      isLocked = false;
      return true;
    } else {
      logMonitor.recordFailure();
      return false;
    }
  }

  public boolean isLocked() {
    return isLocked;
  }
}
