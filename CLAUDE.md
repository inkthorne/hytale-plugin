# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Hytale server plugin template using Java 25 and Gradle. It provides an example `/hello` slash command as a starting point for plugin development.

## Hytale API Documentation

**IMPORTANT:** Before parsing `HytaleServer.jar`, check `docs/overview.md` for pre-extracted API documentation and navigation to specific topics.

## Build Commands

**Use the batch scripts for building and deploying:**
```batch
clean.bat      # Clean build artifacts
build.bat      # Build the plugin JAR (output: build/libs/example-plugin.jar)
deploy.bat     # Copy JAR to Hytale mods folder
```

Or use Gradle directly:
```bash
./gradlew clean          # Clean build artifacts
./gradlew shadowJar      # Build the plugin JAR
./gradlew compileJava    # Compile without packaging
```

## Local Paths

- **Hytale server directory**: `%APPDATA%\Hytale\install\release\package\game\latest\Server\`
- **HytaleServer.jar source**: `%APPDATA%\Hytale\install\release\package\game\latest\Server\HytaleServer.jar`

## Deployment

Copy `build/libs/example-plugin.jar` to the Hytale mods folder:
```
%APPDATA%\Hytale\UserData\Mods\
```

## Architecture

### Plugin Structure
- **Entry point**: `ExamplePlugin` extends `JavaPlugin`, requires constructor with `JavaPluginInit`
- **Commands**: Extend `AbstractPlayerCommand`, register in plugin's `setup()` method via `getCommandRegistry().registerCommand()`
- **Manifest**: `src/main/resources/manifest.json` defines Group, Name, and Main class

### Key Hytale API Patterns
- Commands execute asynchronously (inherit from `AbstractAsyncCommand`)
- Player messaging: `playerRef.sendMessage(Message.raw("text"))`
- Command signature: `execute(CommandContext, Store<EntityStore>, Ref<EntityStore>, PlayerRef, World)`

### Dependencies
- `libs/HytaleServer.jar` - Hytale server API (compileOnly, copied from game installation)
- ShadowJar plugin packages dependencies into single JAR

## Template Placeholders

When customizing this template, find and replace:
- `com.example.myplugin` - Your package name
- `ExamplePlugin` - Your plugin class name
- `ExampleCommand` - Your command class name
- `YourName` - Your name in manifest.json
- `example-plugin` - Your JAR name in build.gradle
