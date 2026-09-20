# Cyber Heist Simulator

A security-bypass simulation in Java, modelling a layered vault as composed objects that
each enforce their own access rules.

## What it does

A vault sits behind a stack of security layers. Getting in means defeating each layer in
turn — the right access key, the right hacker tool — while a log monitor records every
attempt. Because each layer is its own object enforcing its own rule, the vault's difficulty
is a function of how many layers you wrap around it rather than a branch in a method.

- Vaults protected by an arbitrary number of security layers
- Access keys and hacker tools as the means of defeating layers
- A log monitor observing and recording attempts
- Simulation driver that runs a heist end to end

## Architecture

```
Vault                  the target - holds contents behind its layers
SecurityLayer          one defence, enforcing one rule
AccessKey              credential for defeating a layer
HackerTool             the attacker's instrument
LogMonitor             records attempts
CyberHeistSimulation   driver
```

The design keeps each security concern in one place: a layer knows how it can be defeated
and nothing else, the vault knows its layers and nothing about how they work, and the
monitor observes without participating. Adding a new kind of defence means writing one
class.

## Running it

Requires Java 17+ and Maven.

```bash
mvn clean compile exec:java
```
