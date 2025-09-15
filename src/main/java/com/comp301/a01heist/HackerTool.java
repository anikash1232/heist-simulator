package com.comp301.a01heist;

import java.lang.reflect.Field;

public class HackerTool {
  public static void tryBreakVault(Vault vault) {
    try {
      Field f = Vault.class.getDeclaredField("secret");
      f.setAccessible(true);
      Object value = f.get(vault);
      System.out.println("Hacked Secret: " + value);
    } catch (Exception e) {
      System.out.println("HackerTool failed.");
    }
  }
}
