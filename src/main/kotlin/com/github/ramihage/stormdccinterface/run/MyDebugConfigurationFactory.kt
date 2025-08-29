package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class MyDebugConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "MyDebugConfigurationFactory"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return MyDebugRunConfiguration(project, this, "My Debug Run")
    }

    override fun getOptionsClass(): Class<out BaseState> {
        return MyDebugRunConfigurationOptions::class.java
    }
}