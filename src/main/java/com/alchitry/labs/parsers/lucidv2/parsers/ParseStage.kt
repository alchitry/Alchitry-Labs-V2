package com.alchitry.labs.parsers.lucidv2.parsers

enum class ParseStage {
    Globals, // parse global blocks
    Modules, // module types
    AlwaysIO, // module internal (always blocks)
    Drivers, // check for driven signals
    Instantiated, // check instantiated modules with actual parameters
    Evaluation // runtime
}