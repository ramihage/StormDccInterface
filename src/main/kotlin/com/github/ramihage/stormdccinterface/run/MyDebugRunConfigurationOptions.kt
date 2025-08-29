package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.configurations.RunConfigurationOptions

class MyDebugRunConfigurationOptions : RunConfigurationOptions() {
    private val scriptPath = string("").provideDelegate(this, "scriptPath")
    
    fun getScriptPath(): String? = scriptPath.getValue(this)
    fun setScriptPath(value: String?) = scriptPath.setValue(this, value)
}
