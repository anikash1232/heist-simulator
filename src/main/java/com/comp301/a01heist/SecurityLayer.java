package com.comp301.a01heist;

public class SecurityLayer {
  private final String passHash;
  private final String biometric;

  public SecurityLayer(String passphrase, String biometric) {
    this.passHash = hash(passphrase);
    this.biometric = biometric;
  }

  public boolean authenticate(String passAttempt, String bioAttempt) {
    String attemptHash = hash(passAttempt);
    return passHash.equals(attemptHash) && biometric.equals(bioAttempt);
  }

  private String hash(String s) {
    return Integer.toHexString(s.hashCode());
  }
}
