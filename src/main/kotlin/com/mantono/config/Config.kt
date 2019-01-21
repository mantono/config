package com.mantono.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.lang.RuntimeException
import kotlin.collections.Map.Entry

interface Config {
    /**
     * Return a value for the key @param key, or a String representation
     * of @param default if no value can be found for the given key
     */
    operator fun get(key: String, default: Any): String

    /**
     * Return a value for the key @param key, or throw a [RuntimeException]
     * if no value can be found for the given key
     */
    operator fun get(key: String): String

    /**
     * Return a value for the key @param key, or a null if no value can
     * be found for the given key
     */
    fun getOrNull(key: String): String?
}

internal fun loadConfig(): Config {
    val appEnv: String = System.getProperty("APP_ENV") ?: System.getenv("APP_ENV") ?: "development"
    val env = Environment(appEnv)
    return ConfigFactory.load().forEnvironment(env)
}

private fun Config.forEnvironment(env: Environment): Config {
    val envKeyPrefix: String = env.name.toLowerCase()
    return if(hasPath(envKeyPrefix)) {
        this
            .getConfig(env.name.toLowerCase())
            .withFallback(this)
    } else {
        this
    }
}