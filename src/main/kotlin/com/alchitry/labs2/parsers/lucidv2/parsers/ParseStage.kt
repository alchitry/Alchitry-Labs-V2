package com.alchitry.labs2.parsers.lucidv2.parsers

enum class ParseStage(val filter: WalkerFilter) {
    ModuleInternals(WalkerFilter.SkipGlobals), // module internal (always blocks)
    Drivers(WalkerFilter.SkipGlobals), // check for driven signals
    Evaluation(WalkerFilter.join(WalkerFilter.SkipControlBlocks, WalkerFilter.SkipGlobals)), // runtime
    ErrorCheck(WalkerFilter.SkipGlobals), // IDE error check
    Convert(WalkerFilter.ModulesOnly) // convert to Verilog
}