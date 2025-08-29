package com.github.ramihage.stormdccinterface.run

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner

class MyDebugRunState(
    environment: ExecutionEnvironment,
    private val configuration: MyDebugRunConfiguration
) : com.intellij.execution.configurations.CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val commandLine = GeneralCommandLine().apply {
            // Example: running a simple script
            // Replace with your actual debug command
            exePath = "python" // or whatever executable you need
            addParameter("-u") // unbuffered output for debugging
            addParameter(configuration.scriptPath ?: "")
            
            // Set working directory - use the project from the inherited environment
            withWorkDirectory(configuration.project.basePath)
        }
        
        return OSProcessHandler(commandLine)
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        val result = super.execute(executor, runner)
        
        // You can add debug-specific logic here
        if (executor.id == "Debug") {
            // Add debug-specific handling
            println("Starting debug session for: ${configuration.scriptPath}")
        }
        
        return result
    }
}
