package com.comp301.a01heist;

import java.util.Random;

public class AccessKey {
  private int uses;
  private final int key;

  public AccessKey() {
    this.key = new Random().nextInt(100000, 1_000_000); // 6-digit
    this.uses = 0;
  }

  public boolean isValid() {
    uses++;
    return uses % 3 != 0;
  }
}
