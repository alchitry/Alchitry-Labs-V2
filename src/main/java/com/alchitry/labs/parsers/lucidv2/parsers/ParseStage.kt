package com.alchitry.labs.parsers.lucidv2.parsers

enum class ParseStage {
    Globals, // parse global blocks
    Modules, // module types
    ModuleInternals, // module internal (always blocks)
    Drivers, // check for driven signals
    Evaluation // runtime
}