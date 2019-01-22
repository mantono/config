package com.mantono.args

data class CommandLineArguments(val flags: Map<String, Any> = emptyMap(), val args: List<String> = emptyList()) {
    fun add(arg: String): CommandLineArguments = this.copy(args = args + arg)
    fun add(flag: String, arg: Any): CommandLineArguments = this.copy(flags = flags + (flag to arg))
    inline operator fun <reified T> get(flag: String): T = flags[flag] as T
}