package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class MyDebugRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<MyDebugRunConfigurationOptions>(project, factory, name) {

    override fun getOptions(): MyDebugRunConfigurationOptions {
        return super.getOptions() as MyDebugRunConfigurationOptions
    }
    
    var scriptPath: String?
        get() = options.getScriptPath()
        set(value) = options.setScriptPath(value)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MyDebugRunConfigurationEditor()
    }

    override fun checkConfiguration() {
        if (scriptPath.isNullOrBlank()) {
            throw RuntimeConfigurationException("Script path must be specified")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return MyDebugRunState(environment, this)
    }
}
