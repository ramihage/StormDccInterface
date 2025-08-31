package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class MyConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "MyConfigurationFactory"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return MyDebugRunConfiguration(project, this, "Run In DCC")
    }

    override fun getOptionsClass(): Class<out BaseState> {
        return MyDebugRunConfigurationOptions::class.java
    }
}