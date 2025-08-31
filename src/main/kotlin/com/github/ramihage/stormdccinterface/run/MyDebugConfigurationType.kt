package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.icons.AllIcons

class MyDebugConfigurationType : ConfigurationTypeBase(
    "MyDebugConfiguration",
    "My Debug Runner", 
    "Custom debug run configuration",
    AllIcons.Actions.StartDebugger
) {
    init {
        addFactory(MyConfigurationFactory(this))
    }
}
