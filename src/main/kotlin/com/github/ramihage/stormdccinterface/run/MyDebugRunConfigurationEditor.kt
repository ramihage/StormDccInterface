package com.github.ramihage.stormdccinterface.run

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class MyDebugRunConfigurationEditor : com.intellij.openapi.options.SettingsEditor<MyDebugRunConfiguration>() {
    
    private val scriptPathField = TextFieldWithBrowseButton()
    
    override fun createEditor(): JComponent {
        return panel {
            row("Script path:") {
                cell(scriptPathField)
                    .comment("Select the script file to debug")
            }
        }
    }

    override fun resetEditorFrom(configuration: MyDebugRunConfiguration) {
        scriptPathField.text = configuration.scriptPath ?: ""
    }

    override fun applyEditorTo(configuration: MyDebugRunConfiguration) {
        configuration.scriptPath = scriptPathField.text
    }
}
