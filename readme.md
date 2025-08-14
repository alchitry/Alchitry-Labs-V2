# Alchitry Labs V2

Welcome to the Alchitry Labs V2 repo! It is now in beta, so give it a try and report any issues to
the [issues pages](https://github.com/alchitry/Alchitry-Labs-V2/issues).

This is a full rewrite of the [original Alchitry Labs](https://github.com/alchitry/Alchitry-Labs).

The original was written in Java (with some of it being later converted to Kotlin) and used SWT for the UI.
This rewrite is written purely in Kotlin,
which has already helped avoid many null pointer issues that plagued the original.

SWT also hasn't aged well.
There was a weird bug/change that broke tool-tip windows that were used to show what an error was when hovering over it.
This prevented it being updated slowly making it more and more outdated.

This rewrite is using [Jetpack Compose](https://github.com/JetBrains/compose-multiplatform) which I've been using
fairly extensively on other projects and will let me build much better custom UI elements.
For example,
the old [wave viewer](https://github.com/alchitry/Alchitry-Labs/blob/master/src/com/alchitry/labs/widgets/Waves.java)
was fairly limited even though the code for it is already a complicated mess.
Compose should let me rewrite this in a much cleaner way.

## Tools Used

* Linux or Windows (macOS can be used, but the proprietary build tools rely upon Vivado/iceCube2 and do not support
  macOS)
* [Intellij](https://www.jetbrains.com/idea/)
* Gradle
* Kotlin
* [Antlr 4](https://www.antlr.org/) ([Intellij Plugin](https://plugins.jetbrains.com/plugin/7358-antlr-v4))
* [Jetpack Compose](https://github.com/JetBrains/compose-multiplatform)

## Setup

Download and Install any Java JDK version 22.

Download and Install 7-zip and add the installation folder to your Environment variables / System path. On Windows the folder is typically `C:\Program Files\7-Zip`.

Clone the repo and open it as a project in Intellij. The gradle build files should cause Intellij to install the correct
versions of all libraries and Kotlin.

You may want to install the Antlr plugin for syntax highlighting of the Lucid.g4 grammar. (File->Settings->Plugins)

Once the gradle sync is done, you should be able to run the tests by opening the test file and clicking the green arrow
in the code editor gutter or right-clicking on the test folder in the project tree and selecting "Run 'Tests in ...'"

You should download the latest [oss-cad-suite build](https://github.com/alchitry/oss-cad-suite-build) by running the
gradle task `download-oss-cad-suite` in the `build setup` group.

To run the GUI, open the file `GUI.kt` at this path `src/main/kotlin/com/alchitry/labs2/GUI.kt` and run the main function.
This will fail as `app.dir` isn't set, but it should create the run configuration.

Edit that configuration by adding `-Dapp.dir=includes/linux-x64` to the `VM options` field.
Replace the `linux-x64` folder with the version for your OS (look in the `includes` folder for the names).

The current environment is set up for Linux, and you may need to change some things if you are on something else.

## Building

Full builds are done using [Conveyor](https://www.hydraulic.dev/).
You will need this installed if you plan on making an installable package.

There are also two files you need to create alongside the `conveyor.conf` file.

First, `secrets.properties` should look something like the following.

```aiignore
conveyorRootKey=YOUR_ROOT_KEY
```

See [the docs](https://conveyor.hydraulic.dev/16.0/configs/keys-and-certificates/#keys-in-conveyor) for more info on
keys.

Second, `conveyor-local.conf` should contain an oauth token to your GitHub repo.
See [the docs](https://conveyor.hydraulic.dev/16.0/configs/download-pages/#publishing-through-github) for what to do.

It should look something like the following.

```aiignore
app.site.github.oauth-token = YOUR_TOKEN_HERE
```

I also have the `app.windows.store` config in this file.

## Goals

To summarize, these are the main points I hope to drastically improve with this rewrite.

* Cleaner code - fewer bugs, easier to add features
* Improved modern UI
* New [Lucid parser](docs/lucid_parser.md) capable of simulation

Each of these points could be expanded into at least a dozen sub-points, and I'll likely write docs for each
subsection.

## Lucid V2

A major component of this rewrite is the introduction of the new [Lucid parser](docs/lucid_parser.md) which supports
Lucid V2.

This new version of Lucid cleans up some aspects of the original which breaks backwards compatibility.
However, that feels like a small price to pay for the *way* cleaner parser.
The new parser builds a much more robust representation of the design that is capable of running fast simulations that
can dramatically speed up creating designs.

The code structure is much improved since this was all planned from the beginning.
The V1 parser was originally only written to translate Lucid to Verilog.
Error checking and other features got bolted on after the fact, creating a nice pile of spaghetti code.

Checkout the [Lucid parser](docs/lucid_parser.md) docs for a deeper dive.

## New Compose Based UI

The new UI is being completely rewritten with [Jetpack Compose](https://github.com/JetBrains/compose-multiplatform).

Compose is a declarative UI toolkit which allows the UI to be created purely in code (no xml or other layout files).
The way it works under the hood is incredibly interesting and worth a read
([part 1](https://medium.com/@banmarkovic/jetpack-compose-under-the-hood-7bb88f08c47e)
and [part 2](https://medium.com/androiddevelopers/under-the-hood-of-jetpack-compose-part-2-of-2-37b2c20c6cdd)).

Compose's flexibility should allow me to create far better UI elements.
For example, I should be able to make tooltips popup showing not only the error you are hovering over but a button to
implement a suggested fix.

I've started the basic work by creating a custom
[CodeEditor](src/main/kotlin/com/alchitry/labs2/ui/code_editor/CodeEditor.kt) that is still efficient with thousands of
lines of code (the built-in TextField's performance scales terribly).
