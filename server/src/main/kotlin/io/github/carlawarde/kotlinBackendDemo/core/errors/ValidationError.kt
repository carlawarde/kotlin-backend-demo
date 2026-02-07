package io.github.carlawarde.kotlinBackendDemo.core.errors

import io.github.carlawarde.kotlinBackendDemo.utils.StringUtils.newLineSeparatedStringList

class ValidationError(val target: String, val errors: List<String>) : DomainError(
    "VAL_001",
    "Your input failed the following validations:\n${newLineSeparatedStringList(errors)}\nPlease address these requirements to continue.",
    "$target has failed validations: ${newLineSeparatedStringList(errors)}"
)
