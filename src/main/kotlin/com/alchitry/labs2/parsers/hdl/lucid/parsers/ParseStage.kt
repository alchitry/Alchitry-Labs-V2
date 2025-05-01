package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.WalkerFilter

enum class ParseStage(val filter: WalkerFilter) {
    ModuleInternals(LucidWalkerFilters.SkipGlobals), // module internal (always blocks)
    Drivers(
        WalkerFilter.join(
            LucidWalkerFilters.SkipGlobals,
            LucidWalkerFilters.SkipRepeatBlocks
        )
    ), // check for driven signals
    Evaluation(WalkerFilter.join(LucidWalkerFilters.SkipControlBlocks, LucidWalkerFilters.SkipGlobals)), // runtime
    ErrorCheck(LucidWalkerFilters.SkipGlobals), // IDE error check
    Convert(LucidWalkerFilters.ModulesOnly), // convert to Verilog
    Prune(WalkerFilter.None) // remove unnecessary branches
}