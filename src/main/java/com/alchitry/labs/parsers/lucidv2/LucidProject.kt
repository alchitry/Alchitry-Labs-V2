package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.StructValue
import com.alchitry.labs.parsers.lucidv2.values.Value

data class LucidProjectTypes(
    val globals: MutableMap<String, Global> = mutableMapOf(),
    val moduleTypes: MutableMap<String, ModuleType> = mutableMapOf()
)

data class Global(
    val name: String,
    val structs: MutableMap<String, StructValue> = mutableMapOf(),
    val constants: MutableMap<String, Value> = mutableMapOf()
)

data class ModuleType(
    val name: String,
    val parameters: MutableMap<String, ParameterType> = mutableMapOf(),
    val inputs: MutableMap<String, SignalWidth> = mutableMapOf(),
    val outputs: MutableMap<String, SignalWidth> = mutableMapOf(),
    val inouts: MutableMap<String, SignalWidth> = mutableMapOf()
)

data class ModuleInstance(
    val module: ModuleType,
    val parameters: MutableMap<String, Value> = mutableMapOf(),
    val dffs: MutableMap<String, SignalWidth> = mutableMapOf(),
    val fsms: MutableMap<String, SignalWidth> = mutableMapOf(),
    val signals: MutableMap<String, SignalWidth> = mutableMapOf(),
    val modules: MutableMap<String, ModuleInstance> = mutableMapOf()
)

data class ParameterType(
    val name: String,
    val constraint: String?
)