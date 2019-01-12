package com.mantono.config

import java.io.File

enum class Environment {
	PRODUCTION,
	DEVELOPMENT,
	STAGING,
	TEST;

	companion object {
		operator fun invoke(env: String = SystemConfig["APP_ENV"]): Environment {
			return when(val appEnv = env.trim().toUpperCase()) {
				"TEST" -> TEST
				"DEV" -> DEVELOPMENT
				"STAGE" -> STAGING
				"PROD" -> PRODUCTION
				else -> valueOf(appEnv)
			}
		}
	}
}