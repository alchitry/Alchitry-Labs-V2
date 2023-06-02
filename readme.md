# Lucid Parser V2

This code isn't intended to be used on its own and it will be incorporated into Alchitry Labs 2 in the future.

## Background

Lucid was first developed for the Mojo by Justin Rajewski when he was still in college. Its purpose was to provide an
alternative to the heavy existing HDL languages to make simple things simple.

The first attempt at creating a parser replied upon regex and some logic. It was a mess that was cleaned up with the
discovery of ANTLR and real grammars.

The goal for the original tools was to simply convert Lucid to Verilog. While writing that, some error checking started
to get added.

At first, only the widths of signals and expressions was being tracked. However, as time went on, custom Lucid functions
were added which required values to actually be computed. These would be replaced with a constant value at translation
time but the tools needed to be able to compute generic expressions. This was when
[`CosntExprParser`](https://github.com/alchitry/Alchitry-Labs/blob/master/src/com/alchitry/labs/parsers/tools/lucid/ConstExprParser.java)
was created.

The scope of what was being parsed and what errors were being checked for kept increasing along with the tech debt.

The result is the mess that is the Lucid V1 interpreter. There aren't well-defined responsibilities of each parser with
many of them being redundant. This has led to some hard to fix bugs and many that are harder still to track down.

This was the motivation for doing a full rewrite with lots of new features in mind.

### Lucid V2 Goals

* Easier to understand and maintain
* Provide a Lucid simulator
* Simplify un-necessary aspects of Lucid V1
* Stability
* Robust error checking - no errors means exported Verilog should build

## Tools Used

* Linux or Windows (macOS can be used for the parser but the build tools rely upon Vivado/iceCube2 and do not support
  macOS)
* [Intellij](https://www.jetbrains.com/idea/)
* Gradle
* Kotlin (manged in gradle script)
* [Antlr 4](https://www.antlr.org/) ([Intellij Plugin](https://plugins.jetbrains.com/plugin/7358-antlr-v4))

## Setup

Clone the repo and open it as a project in Intellij. The gradle build files should cause Intellij to install the correct
versions of all libraries and Kotlin.

You may want to install the Antlr plugin for syntax highlighting of the Lucid.g4 grammar. (File->Settings->Plugins)

Once the gradle sync is done, you should be able to run the tests by opening the test file and clicking the green arrow
in the code editor gutter or right clicking on the test folder in the project tree and selecting "Run 'Tests in ...'"

## Lucid V1 Background

* https://alchitry.com/lucid-reference
* https://alchitry.com/lucid

The legacy code for Lucid V1 can be
found [here](https://github.com/alchitry/Alchitry-Labs/tree/master/src/com/alchitry/labs/parsers/tools/lucid).

This code was originally written to simply check the width of signals to see if they matched but kept getting features
bolted on, so now it is a bit of a mess. The new version is drastically different but some things may still be helpful
as a reference.

## The Grammar

The grammar was written using ANTLR 4. ANTLR generates a parser that is capable of turning text into a meaningful
tree representation that the parsers can then walk.

See the Lucid.g4 file for the actual grammar.

## Value Representation

Lucid values of are made up of bits. Each bit can take one of five values. These are represented in the `Bit` class.

`B0` and `B1` are the obvious 0 and 1 values.

`Bx` is used when the value could be either 0 or 1 but we either don't care (when assigning it directly) or don't know
(bad computation).

`Bz` is used to represent high-impedance and should only ever be used by a top-level output or inout.

All values are packed into the `Value` class. This is a sealed (abstract) class that is made up of a few concrete
classes. Every `Value` has the boolean property `constant` to designate if this value could change. This is for error
checking. Some functions, like `$clog2()`, can only be used with constant values as they must be computed at synthesis
time.

The `SimpleValue` class is another sealed class that is used to represent binary values. The value can be made up of
multiple bits via the `BitListValue` class or a single bit via the `BitValue` class. The `SimpleValue` class has a
property `signed` that indicates if this value should be interpreted as 2's complement (negative numbers) or not.

`SimpleValue` also has some helper functions for manipulating the values such as `or`, `and`, and `xor` as well as
comparison like `isGreaterOrEqualTo`.

`ArrayValue` is used to represent arrays. Each element in the array *must* be the same size. The elements themselves
are `Value`s and could be another `ArrayValue` for even deeper arrays or `StructValue`. The elements should never be a
`BitValue` though as an array of `BitValue` should be represented with `BitListValue`.

`StructValue` is used to represent a key-value map of signals similar to a struct in C. The declaration looks like this.

```  
struct in {
    addr[28],
    cmd[3],
    en,
    wr_data[128],
    wr_en,
    wr_mask[16]
  }
```

This would define a struct with name `in`. The various components of it can be accessed with the
syntax `SIGNAL_NAME.STRUCT_MEMBER` for example `SIGNAL_NAME.wr_en` to access the `wr_en` component.

The final `Value` type is `UndefinedValue`. The current only use for this is for module parameters. Module parameters
are used to make modules more generic by allowing a constant to be provided at the time it is instantiated (used). The
parameter's value would be set to `UndefinedValue` when the module is being evaluated but hasn't been instantiated yet.
This value will be replaced with a real value once the module is instantiated and error checking on any `UndefinedValue`
can properly happen then.

When possible, a known width can be set inside the `UndefinedValue` by passing in the `width` parameter. This is helpful
for early error checking.

#### SignalWidth

The class `SignalWidth` is used to represent the width of a signal without an associated value. This is mainly used for
error checking purposes.

There are matching types of `SignalWidth` for each corresponding `Value`. `SimpleWidth`, `BitWidth`, `BitListWidth`,
`ArrayWidth`, `StructWidth`, and `UndefinedSimpleWidth`.

`UndefinedSimpleWidth` is used to specify when we know a value is a 1D array of bits, but we don't know how many. This
is the default width for any module parameters.

The `Value` class has as property `signalWidth` that will return the `SignalWidth` for the value. This value is
typically computed from the `Value` directly except for `UndefinedValued` which simply uses its `width` property.

## Signals

All the `Value` classes are immutable. To represent a changing value, the `Signal` class is used. This class is used
for things like `sig` types as well as module ports. These can be _connected_ together so the value of one is fed into
the next automatically during simulation.

A portion of a `Signal` can be selected with the `select()` function. This resulting `SubSignal` can be used to read
and write the selected portion of its parent `Signal`. Most of the time, we don't care if we are using a `Signal` or a
`SubSignal` so the sealed interface `SignalOrSubSignal` is used to encompass both types.

`Signal`s resent their values though a `Flow` that can be monitored to know whenever its value changes. This is used to
automatically queue up the evaluation of everything that depends on it.

### DynamicExpr

`DynamicExpr` are used to represent a dynamic value that is connected to a port of a module instance. When a module is
instantiated you may see something like this `modType myMod (.port(signal + 10))`. Here the expression `signal + 10`
would be represented by a `DynamicExpr` and connected to the module's port, `port`.

These will automatically queue themselves up for re-evaluation if any of their dependencies change.

### SignalParent

Many objects are the parent to signals. For example, a module instance is a parent to the signals that make up its
ports.
A dff is a parent to its d and q signals. This is represented using the `SignalParent` interface.

A `SignalParent` is responsible for providing its siblings via the `getSignal()` function. This function returns a
`SignalOrParent`. This interface is a sealed interface encompassing the `Signal` class and `SignalParent` interface.

## Parser Overview

The new parser is broken up into many small parsers that each perform a specific job.

These pieces are coordinated via the main `ProjectContext` and the sub `Lucid____Context` classes.

When a full project is going to be parsed, there are a few different passes that take place.

First, all the text is parsed by the ANTLR parser and turned into a useful tree representation.

Next, all `global` declarations are parsed using the `LucidGlobalContext`.

The next pass is to pull out module types. A module type is defined in the `Module` class and has the name of the
module as well as its parameters and ports. This is done via the `LucidModuleTypeContext`.

The next passes are all done using the same `LucidBlockContext` object. These are the `ModuleInternals` and `Drivers`
passes. The `ModuleInternals` pass looks at all the internals of a module for things like signal declarations, always
blocks, module instances, etc. The `Drivers` pass looks at the output of the `ModuleInternals` pass and checks that all
signals that are expected to be driven are. It also gives warnings for unused signals.

These two passes start at the top-level module in the design and propagate down through the design for every
instantiated module. They are not run on a per-file basis like the first two.

Finally, the last pass is the `Evaluation` pass. This is only run during simulation but each `AlwaysBlock` and
`DynamicExpr` gets its own clone of the `LucidBlockContext` for its `Evaluation` pass.

### LucidExprContext

The `LucidExprContext` is an interface implemented by every context so that the basic expression parsers can be used.
The four parses that require this are the `ExprParser`, `BitSelectionParser`, `SignalParser`, `ConstantParser`,
`EnumParser`, and `StructParser`.

`ExprParser` is responsible for generating `Value`s for every Lucid expression. An expression could be as simple as
a number or more complex like signals multiple together. Basically, if it falls under the `expr` rule in the Lucid
grammar, the `ExprParser` is responsible for generating a `Value` for it.

`BitSelectionParser` is responsible for generating integer ranges from `bitSelection` contexts. These show up in Lucid
as stuff like `mySignal[5][4:0]` where `[5][4:0]` is a `bitSelection`.

`SignalParser` is responsible for resolving signal names and signal widths. It mostly relies upon the other parsers to
provide the `SignalOrParent` via the `resolveSignal()` function of the `LucidExprContext` but it takes the base signal
and drills down into it when sub-signals are selected.

Its main job is to provide the `resolve()` function that converts `SignalContext` to `SignalOrSubSignal`.

`ConstantParser` and `EnumParser` are responsible for constant and enum declarations respectively.

Finally, `StructParser` is responsible for struct declarations and resolving struct types.

### LucidGlobalContext and LucidModuleTypeContext

These two contexts are pretty simple and mostly rely upon the above parsers to parse out globals and module types
respectively.

`GlobalParser` generates a `GlobalNamespace` for each global declaration.

`ModuleParser` generates a `Module` for each module declaration.

These are both fed to the `ProjectContext` for later use.

### LucidBlockContext

This context is really the most important one. It builds a model of the module instance or test bench and creates the
necessary signals, always blocks, test blocks, functions, and dynamic expressions for it to be fully simulated.

During the parse stages, it builds out all the internals using the `BlockParser` and `TypesParser`. It also checks that
signals are properly driven with the `SignalDriverParser`.

During the evaluation stage is uses the `BlockEvalutator` to evaluate always blocks as needed.

## Evaluation

Evaluation is broken up into _ticks_.

Inside the `ProjectContext` there is an evaluation queue. During each tick, this queue is processed until empty in
batches. The batched processing is used to detect endless loops in a bad design. Currently, each tick gets 1000
iterations to resolve into a stable state.

The two main `Evaluable`s are the `AlwayBlock` and `DynamicExpr`. Whenever one of the signals that these depend on
changes, it queues itself up to be evaluated in the next batch. Each batch is represented as a `Set` so each
`Evaluable` will only be evaluated at most once per batch.

A tick is started by calling the `processQueue()` function of `ProjectContext`.

Before simulation begins, the `initialize()` function should be called on the top-level module's `LucidBlockContext`.
This will queue every `AlwaysBlock` for an initial evaluation.