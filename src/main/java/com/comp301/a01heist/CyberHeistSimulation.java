package com.comp301.a01heist;

public class CyberHeistSimulation {
    public static void main(String[] args) {
        SecurityLayer layer = new SecurityLayer("hunter2", "retinaScanA");
        AccessKey key = new AccessKey();
        Vault vault = new Vault();

        boolean a1 = vault.accessVault(layer, key, "wrongPass", "retinaScanA");
        System.out.println("Attempt 1: Access granted? " + a1 + " (expected: false)");

        boolean a2 = vault.accessVault(layer, key, "hunter2", "retinaScanA");
        System.out.println("Attempt 2: Access granted? " + a2 + " (expected: true)");

    }
}
