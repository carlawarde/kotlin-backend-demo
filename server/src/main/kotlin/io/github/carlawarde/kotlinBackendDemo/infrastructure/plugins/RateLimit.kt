package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimit(
    limit: Int = 100,
    refill: Duration = 60.seconds
) {
    install(RateLimit) {
        global {
            rateLimiter(limit = limit, refillPeriod = refill)
        }
    }
}