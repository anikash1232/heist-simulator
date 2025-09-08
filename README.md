# Cyber Heist: Break the Vault

## Overview
In this exercise, you’ll take on the role of a white-hat hacker simulating attacks on a futuristic digital vault. The goal is to **design secure systems using strong encapsulation**, and understand how limiting access to internal state improves security.

You'll build several classes that work together to protect a digital secret, and then test whether the protections work under pressure.

## Learning Goals

- Understand and apply encapsulation in Java
- Use constructors, access modifiers, and internal validation
- Protect sensitive data from external access
- Write clean APIs that expose only what is necessary
- Simulate a secure environment using cooperative classes

---

## Class: `AccessKey`

This class represents a time-sensitive digital key that grants access to the secure vault—but only under specific conditions. Each key expires after a few uses, simulating limited-time access credentials.

Implement a class that:
- Tracks how many times it has been used
- Generates a random 6-digit key upon creation
- Provides a method to validate whether the key is still active
- Follows strong encapsulation principles

### Fields

```java
private int uses;
private final int key;
```

- `uses`: A private counter that tracks how many times the key has been checked.
- `key`: A randomly generated 6-digit number. Once created, it must never change or be accessed directly.

Note: Both fields should be `private`. This ensures that no outside class can reset the key or interfere with the use counter.

### Constructor

```java
public AccessKey();
```

- Generate a random number between 100000 and 999999 (inclusive) to simulate a 6-digit access key.
- Use the `java.util.Random` class for this.
- Initialize `uses` to 0.

Example:
```java
int key = new Random().nextInt(100000, 1000000);
```
(We use 1000000 as the upper bound because `nextInt()` is exclusive on the upper end.)

### Method: `isValid()`

```java
public boolean isValid();
```

- This method simulates a key check and tracks how many times the key has been used.
- Each time it is called, increment the `uses` counter.
- Return `true` if this is not the 3rd, 6th, 9th, etc. use.
- Return `false` on every 3rd use.

Logic tip:
```java
return uses % 3 != 0;
```

### Rules and Restrictions

- Do not add a `getKey()` or `getUses()` method. The value of the key and the number of uses are considered internal implementation details and should not be exposed.
- The field `key` is included for realism, but it will not be used unless you're implementing advanced auditing.

### Example Usage

```java
AccessKey key = new AccessKey();
System.out.println(key.isValid()); // true (1st use)
System.out.println(key.isValid()); // true (2nd use)
System.out.println(key.isValid()); // false (3rd use)
System.out.println(key.isValid()); // true (4th use)
```

---

## Class: `SecurityLayer`

This class represents a biometric and password-based access control system used to protect the Vault. It should encapsulate two pieces of sensitive data: a hashed passphrase and a biometric signature.

Your task is to write a secure class that verifies whether a given set of credentials matches what was stored.

### Fields

You will need two private String fields:
- One to hold a hashed version of the passphrase
- One to store the biometric string directly

Both fields should be marked `private` and `final`.

#### What does `final` mean?

When you mark a field as `final`, it can only be assigned once—usually in the constructor. This ensures that once the SecurityLayer is created, its credentials cannot be changed. This is important for both security and encapsulation.

### Constructor

Your constructor should accept two strings:
- A `passphrase`
- A `biometric`

The passphrase should be converted into a secure form using a hashing function before being stored. The biometric string can be stored as-is.

To hash the passphrase, use this line:

```java
Integer.toHexString(passphrase.hashCode());
```

This converts the passphrase into a hexadecimal string, simulating a hash.

### Method: `authenticate`

Create a method that accepts two arguments:
- A passphrase attempt
- A biometric attempt

It should:
- Hash the attempted passphrase in the same way as the constructor
- Check that both the hashed passphrase and the biometric match the originals
- Return `true` if both are correct; otherwise, return `false`

Use `.equals()` to compare the strings.

### Method: `hash` (optional but encouraged)

You may want to create a private helper method that encapsulates the logic for hashing a string. This helps you avoid repeating the same logic in multiple places and improves readability.

### Important Notes

- Do not add any getter methods.
- Do not expose the stored credentials in any way.
- All validation should happen through the `authenticate()` method.
- Do not track or store whether someone has successfully authenticated.

This class should remain stateless after construction and must enforce encapsulation at every level.

---

## Class: `LogMonitor`

This class is responsible for keeping track of failed access attempts to the vault. If too many failures occur, it will permanently lock down access.

This class reinforces the concept of **encapsulation** by keeping its internal state (`failedAttempts`) private and only exposing two well-defined behaviors: recording a failed attempt and checking whether the system is locked.

### Fields

You will need two fields:

1. `failedAttempts`
- This should be a private integer.
- It starts at zero and increases each time someone fails to access the vault.

2. `MAX_FAILURES`
- This should be a `private static final int`.
- It defines the number of failed attempts allowed before the system locks.
- Use the value `5`.

#### Why `private static final`?

- `private`: No other class should know or change this limit.
- `static`: All instances of `LogMonitor` share the same value.
- `final`: The limit should never change once defined.

This constant acts as a configurable rule for the class—without exposing it directly.

### Method: `recordFailure`

This method should increment the `failedAttempts` counter by one.

Each time this method is called, it simulates a failed attempt to access the vault.

There are **no parameters** and it **does not return anything**.

### Method: `isLocked`

This method should return `true` if the number of failed attempts has reached or exceeded `MAX_FAILURES`.

It should return `false` otherwise.

Do not allow any other class to access or reset the counter—`failedAttempts` must remain hidden.

### Important Notes

- The counter should not reset once the vault is locked.
- There should be **no getters or setters** for `failedAttempts`.
- The only way to modify the internal state is by calling `recordFailure()`.
- The only way to observe the state is through `isLocked()`.

This class is a perfect example of **information hiding**: it tracks state internally but exposes only the functionality needed to interact with that state in a safe way.

---

## Class: `Vault`

The `Vault` class represents the secure digital vault that stores a classified secret string. It is the core of the "Cyber Heist" simulation, and access to it is tightly controlled by two factors:

1. A valid authentication check through a `SecurityLayer`
2. A valid (non-expired) `AccessKey`

This class also uses a `LogMonitor` to keep track of failed access attempts. After too many failed attempts, the vault will permanently lock.

Your goal is to write this class in a way that enforces encapsulation and simulates secure access control.

### Fields

You will need the following **private fields**:

- A `String` called `secret`.
  - This is the secret being protected.
  - It should never be returned, printed, or exposed in any way.

- A `boolean` called `isLocked`, initialized to `true`.
  - This tracks whether the vault is currently unlocked or not.
  - The vault begins in a locked state and only unlocks after successful access.

- A `LogMonitor` object.
  - This keeps track of failed access attempts.
  - Once too many failures occur, the vault enters permanent lockdown.

All fields must be private. `secret` should also be `final` since it never changes.

### Constructors

You should have two constructors, chained together using the `this` keyword such that the default constructor calls a specialized constructor.  
- The specialized constructor should take in the `secret` code and set it to the instance variable's value.  
- The default constructor should initialize it to `"TOP_SECRET_LAUNCH_CODES"`


### Method: `accessVault`

```java
public boolean accessVault(SecurityLayer layer, AccessKey key, String passAttempt, String bioAttempt);
```

This is the method clients use to attempt to unlock the vault.

It should follow this process:

1. **Check for lockdown**:  
   If `logMonitor.isLocked()` is `true`, immediately return `false`.

2. **Authenticate credentials**:  
   Use `layer.authenticate(passAttempt, bioAttempt)` to check whether the credentials are correct.

3. **Check AccessKey**:  
   If the credentials were correct, call `key.isValid()` to check whether the key is still valid (has not expired).

4. **Grant access or record failure**:
- If **both** authentication and access key are valid:
  - Set `isLocked = false`
  - Return `true`
- Otherwise:
  - Call `logMonitor.recordFailure()` to track the failure
  - Return `false`

You should not expose or modify any internal field directly from outside the class.

### Method: `isLocked`

```java
public boolean isLocked();
```

This method returns `true` if the vault is still locked and `false` if it has been successfully unlocked.

This is the only safe way to externally observe the state of the vault.

### Summary

- Enforces multi-factor access: both valid credentials and a valid key are required.
- Tracks failed attempts through `LogMonitor` but does not expose any counters directly.
- Prevents retrying access after permanent lockdown.
- Does not allow any method to access or reveal the vault's secret content.

This class brings together everything you've written so far — secure input validation, encapsulation, and access control logic.

---
## Class: `CyberHeistSimulation`

This class contains the `main` method that runs your simulation. It demonstrates how the `Vault`, `SecurityLayer`, and `AccessKey` classes interact during an access attempt.

The purpose of this simulation is to show both:
- A **failed access attempt** (due to incorrect credentials)
- A **successful access attempt** (with valid credentials and an unused access key)

This class does not perform any validation itself—it simply uses the public interfaces of your other classes.

# Vault Access Simulation Requirements

You are tasked with simulating a secure vault system that combines multiple layers of authentication and access control. Your solution should include the following components and behaviors:

- **Vault**  
  Represents the protected resource. Access to the vault is only possible through an authentication method.

- **SecurityLayer**  
  Validates credentials.
    - Correct passphrase: `"hunter2"`
    - Correct biometric signature: `"retinaScanA"`

- **AccessKey**  
  Acts as a digital key.
    - Valid for two consecutive uses
    - Automatically expires on every third attempt
    - Resets its validity cycle after expiring

- **Access Attempts**  
  Every vault access attempt must check:
    1. Whether the credentials match the `SecurityLayer`
    2. Whether the `AccessKey` is currently valid

- **Program Demonstration**  
  Your program should clearly show the following scenarios:
    - An attempt with incorrect credentials should fail, regardless of the `AccessKey` state.
    - A subsequent attempt with the correct credentials should succeed, provided the `AccessKey` is valid.
    - The result of each attempt should be printed, showing whether access was granted and why.

### Expected Output

```
Attempt 1: Access granted? false (expected: false)
Attempt 2: Access granted? true (expected: true)
```

### Additional Notes

- This class demonstrates **normal use** of a secure system
- You are not expected to write new logic — just set up and call your existing classes correctly.
- This is a great place to debug whether your `Vault`, `SecurityLayer`, `AccessKey`, and `HackerTool` are working properly together.

Let your output guide you!


## BONUS OPPORTUNITY: 2pts

---

## Class: `HackerTool`

In this challenge, you’ll write a class that simulates a **reflection-based attack** on the `Vault` class.

Even though `Vault.secret` is `private` and `final`, you’ll use Java’s `java.lang.reflect` package to bypass access control and extract the value.

This exercise demonstrates how powerful — and dangerous — reflection can be if used improperly.

---

### Your Goal

Implement the following class and method:

```java
package com.comp301.a01heist;

public class HackerTool {
    public static void tryBreakVault(Vault vault) {
        // Use reflection to access the Vault's private "secret" field
    }
}
```

When executed, this method should:
- Locate the private `secret` field inside the given `Vault` object.
- Force it accessible, even though it’s private.
- Extract and print its value with this format:

```
Hacked Secret: TOP_SECRET_LAUNCH_CODES
```

If anything fails (e.g. wrong field name or security block), print:

```
HackerTool failed.
```


---

### Example Output

If everything works:
```
Hacked Secret: TOP_SECRET_LAUNCH_CODES
```

If something goes wrong (e.g. typo or security block):
```
HackerTool failed.
```


### Add it to your CyberHeistSimulation

7. **Simulate a hacker attack**
   Call your `HackerTool.tryBreakVault(...)` method:
   ```java
   HackerTool.tryBreakVault(vault);
   ```

    - If your `HackerTool` is working correctly, this will extract the secret from the private field using reflection.
    - You should see something like:
      ```
      Hacked Secret: TOP_SECRET_LAUNCH_CODES
      ```


---

### Reminder


This class **violates encapsulation on purpose**. It’s meant as a controlled exercise to demonstrate why access modifiers and secure design matter.

Never use this kind of technique in production code unless you have a very good reason and fully understand the risks.

This class is a genuine example of how to hack a system with a **reflection-based attack**.

