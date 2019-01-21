package com.mantono.args

data class CommandLineArguments(val flags: Map<String, String> = emptyMap(), val args: List<String> = emptyList()) {
    fun add(arg: String): CommandLineArguments = this.copy(args = args + arg)
    fun add(flag: String, arg: String): CommandLineArguments = this.copy(flags = flags + (flag to arg))
}