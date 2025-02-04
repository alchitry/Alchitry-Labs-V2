package com.alchitry.labs2.parsers.acf.types

sealed interface IoStandard {
    val name: String
    val vcco: String
    val inputAnyVcco: Boolean // many IO standards don't require a specific VCCO
    val drive: List<Int>?
    val slew: List<PinSlew>?
    val directions: List<PinDirection>
}

// see https://docs.amd.com/v/u/en-US/ug471_7Series_SelectIO page 98
enum class Artix7IoStandard(
    override val vcco: String,
    override val inputAnyVcco: Boolean = false,
    override val drive: List<Int>? = null,
    override val slew: List<PinSlew>? = PinSlew.entries,
    override val directions: List<PinDirection> = PinDirection.all,
    val vref: String? = null
) : IoStandard {
    BLVDS_25("2.5", slew = null),
    DIFF_HSTL_I("1.5", inputAnyVcco = true, directions = PinDirection.single),
    DIFF_HSTL_I_18("1.8", inputAnyVcco = true, directions = PinDirection.single),
    DIFF_HSTL_II("1.5", inputAnyVcco = true),
    DIFF_HSTL_II_18("1.8", inputAnyVcco = true),
    DIFF_HSUL_12("1.2", inputAnyVcco = true),
    DIFF_SSTL135("1.35", inputAnyVcco = true),
    DIFF_SSTL135_R("1.35", inputAnyVcco = true),
    DIFF_SSTL15("1.5", inputAnyVcco = true),
    DIFF_SSTL15_R("1.5", inputAnyVcco = true),
    DIFF_SSTL18_I("1.8", inputAnyVcco = true, directions = PinDirection.single),
    DIFF_SSTL18_II("1.8", inputAnyVcco = true),
    HSTL_I("1.5", inputAnyVcco = true, directions = PinDirection.single, vref = "0.75"),
    HSTL_I_18("1.8", inputAnyVcco = true, directions = PinDirection.single, vref = "0.9"),
    HSTL_II("1.5", inputAnyVcco = true, vref = "0.75"),
    HSTL_II_18("1.8", inputAnyVcco = true, vref = "0.9"),
    HSUL_12("1.2", inputAnyVcco = true, vref = "0.6"),
    LVCMOS25("2.5", drive = listOf(4, 8, 12, 16)),
    LVCMOS33("3.3", drive = listOf(4, 8, 12, 16)),
    LVDS_25("2.5", slew = null),
    LVTTL("3.3", drive = listOf(4, 8, 12, 16, 24)),
    MINI_LVDS_25("2.5", slew = null),
    PCI33_3("3.3", slew = null),
    PPDS_25("2.5", slew = null, directions = PinDirection.single),
    RSDS_25("2.5", slew = null, directions = PinDirection.single),
    SSTL135("1.35", inputAnyVcco = true, vref = "0.675"),
    SSTL135_R("1.35", inputAnyVcco = true, vref = "0.675"),
    SSTL15("1.5", inputAnyVcco = true, vref = "0.75"),
    SSTL15_R("1.5", inputAnyVcco = true, vref = "0.75"),
    SSTL18_I("1.8", inputAnyVcco = true, directions = PinDirection.single, vref = "0.9"),
    SSTL18_II("1.8", inputAnyVcco = true, vref = "0.9"),
    TMDS_33("3.3", inputAnyVcco = true, slew = null, directions = PinDirection.single)
}

enum class Ice40IoStandard(
    override val vcco: String,
    override val inputAnyVcco: Boolean,
    override val drive: List<Int>?,
    override val slew: List<PinSlew>?,
    override val directions: List<PinDirection>
) : IoStandard {
    LVCMOS33("3.3", false, null, null, PinDirection.all),
}