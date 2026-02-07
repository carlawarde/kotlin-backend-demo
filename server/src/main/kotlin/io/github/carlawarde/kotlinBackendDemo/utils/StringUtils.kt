package io.github.carlawarde.kotlinBackendDemo.utils

object StringUtils {

    fun newLineSeparatedStringList(list: List<String>): String = list.joinToString("\n")
}