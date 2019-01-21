package com.mantono.config

data class ContextConfig(private val config: com.typesafe.config.Config): Config {

    constructor(context: String): this(loadConfig().getConfig(context))

    override operator fun get(key: String): String = config.getString(key)
        ?: throw RuntimeException("No value found for property '$key' and no default value given")
    override operator fun get(key: String, default: Any): String = getOrNull(key) ?: default.toString()

    override fun getOrNull(key: String): String? = config.getString(key)

    fun withContext(context: String): ContextConfig = ContextConfig(config.getConfig(context))
}