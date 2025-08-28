package com.github.ramihage.stormdccinterface.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.ramihage.stormdccinterface.MyBundle
import javax.swing.*
import java.awt.Component.LEFT_ALIGNMENT
import javax.swing.BorderFactory
import java.awt.Dimension
import java.awt.BorderLayout
import javax.swing.text.DefaultFormatterFactory
import javax.swing.text.NumberFormatter
import java.text.NumberFormat

class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val stormDccInterfaceWindow = StormDccInterfaceWindow()
        val content = ContentFactory.getInstance().createContent(stormDccInterfaceWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class StormDccInterfaceWindow() {
        companion object{
            var currentDcc: String = "None selected"
        }
        private val dccOptions = arrayOf("Maya", "Houdini", "Nuke")

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            currentDcc = dccOptions[0]
            
            // Create the combo box
            val dccSelector = JComboBox(dccOptions).apply {
                alignmentX = LEFT_ALIGNMENT
            }

            // Create port input field
            val portInputPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                border = BorderFactory.createEmptyBorder(5, 0, 10, 5)
                alignmentX = LEFT_ALIGNMENT
                maximumSize = Dimension(Integer.MAX_VALUE, preferredSize.height)
            }

            val portLabel = JBLabel("Port: ")
            val portInput = JFormattedTextField().apply {
                val numberFormat = NumberFormat.getIntegerInstance().apply {
                    isGroupingUsed = false  // This removes commas
                }
                val numberFormatter = NumberFormatter(numberFormat).apply {
                    allowsInvalid = false
                    minimum = 0
                    maximum = 65535
                }
                setFormatterFactory(DefaultFormatterFactory(numberFormatter))
                columns = 5  // 65535 has 5 digits
                maximumSize = Dimension(preferredSize.width, preferredSize.height)
                text = "4434"
            }

            portInputPanel.add(portLabel, BorderLayout.WEST)
            portInputPanel.add(portInput, BorderLayout.CENTER)
            portInputPanel.add(Box.createHorizontalGlue(), BorderLayout.EAST)
            portInputPanel.isVisible = dccOptions[0] in listOf("Maya", "Nuke")

            // Create a panel for the combo box
            val selectorPanel = JPanel(BorderLayout()).apply {  // Changed to BorderLayout
                add(JBLabel("Select DCC: "), BorderLayout.WEST)
                add(dccSelector, BorderLayout.CENTER)
                alignmentX = LEFT_ALIGNMENT
                maximumSize = Dimension(preferredSize.width, preferredSize.height)
            }

            val dccInfoMap = mutableMapOf(
                "Maya" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandMaya", portInput.text),
                "Houdini" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandHoudini"),
                "Nuke" to MyBundle.message("stormdccinterface.command.ListenOnPortCommandNuke", portInput.text)
            )
            val dccCloseInfoMap = mutableMapOf(
                "Maya" to MyBundle.message("stormdccinterface.command.ClosePortCommandMaya", portInput.text),
                "Houdini" to MyBundle.message("stormdccinterface.command.ClosePortCommandHoudini"),
                "Nuke" to MyBundle.message("stormdccinterface.command.ClosePortCommandNuke", portInput.text)
            )

            // Create dynamic label
            val labelText = MyBundle.message("stormdccinterface.command.Title", dccOptions[0])
            val dynamicLabel = JBLabel(labelText).apply {
                border = BorderFactory.createEmptyBorder(10, 0, 10, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            // Create the info text area
            val infoTextArea = JTextArea().apply {
                text = dccInfoMap[dccOptions[0]] ?: ""
                isEditable = false
                lineWrap = true
                wrapStyleWord = true
                background = UIManager.getColor("TextArea.background")
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                alignmentX = LEFT_ALIGNMENT
            }
            
            // Create scroll pane for text area
            val scrollPane = JScrollPane(infoTextArea).apply {
                border = BorderFactory.createLoweredBevelBorder()
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(Integer.MAX_VALUE, 100)
                maximumSize = Dimension(Integer.MAX_VALUE, 100)
                background = UIManager.getColor("TextArea.background")
                viewport.background = UIManager.getColor("TextArea.background")
                alignmentX = LEFT_ALIGNMENT
            }

            // Create port closing instructions widgets
            val closeLabelText = MyBundle.message("stormdccinterface.command.CloseTitle")
            val closeLabel = JBLabel(closeLabelText).apply {
                border = BorderFactory.createEmptyBorder(10, 0, 10, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            val closeInfoTextArea = JTextArea().apply {
                text = dccCloseInfoMap[dccOptions[0]] ?: ""
                isEditable = false
                lineWrap = true
                wrapStyleWord = true
                background = UIManager.getColor("TextArea.background")
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                alignmentX = LEFT_ALIGNMENT
            }

            // Create scroll pane for text area
            val closeScrollPane = JScrollPane(closeInfoTextArea).apply {
                border = BorderFactory.createLoweredBevelBorder()
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(Integer.MAX_VALUE, 100)
                maximumSize = Dimension(Integer.MAX_VALUE, 100)
                background = UIManager.getColor("TextArea.background")
                viewport.background = UIManager.getColor("TextArea.background")
                alignmentX = LEFT_ALIGNMENT
            }

            // Add action listener to combo box
            dccSelector.addActionListener {
                val selectedDcc = dccSelector.selectedItem as String
                currentDcc = selectedDcc
                infoTextArea.text = dccInfoMap.getOrDefault(selectedDcc, "")
                dynamicLabel.text = MyBundle.message("stormdccinterface.command.Title", selectedDcc)
                closeInfoTextArea.text = dccCloseInfoMap.getOrDefault(selectedDcc, "")
                portInputPanel.isVisible = selectedDcc in listOf("Maya", "Nuke")
            }

            // Add document listener to port input field for real-time updates
            portInput.document.addDocumentListener(object : javax.swing.event.DocumentListener {
                override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updatePortInfo()
                override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updatePortInfo()
                override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updatePortInfo()
                
                private fun updatePortInfo() {
                    val selectedPort = portInput.text
                    val selectedDcc = dccSelector.selectedItem as String
                    
                    // Update the map values with the new port
                    when (selectedDcc) {
                        "Maya" -> {
                            dccInfoMap[selectedDcc] = MyBundle.message("stormdccinterface.command.ListenOnPortCommandMaya", selectedPort)
                            dccCloseInfoMap[selectedDcc] = MyBundle.message("stormdccinterface.command.ClosePortCommandMaya", selectedPort)
                        }
                    }
                    
                    // Update the displayed text
                    infoTextArea.text = dccInfoMap[selectedDcc] ?: ""
                    closeInfoTextArea.text = dccCloseInfoMap[selectedDcc] ?: ""
                }
            })

            // Add components to the main panel
            add(Box.createRigidArea(Dimension(0, 5)))  // Add some spacing at the top
            add(selectorPanel)
            add(portInputPanel)
            add(dynamicLabel)
            add(scrollPane)
            add(closeLabel)
            add(closeScrollPane)
            add(Box.createVerticalGlue())
        }
    }
}