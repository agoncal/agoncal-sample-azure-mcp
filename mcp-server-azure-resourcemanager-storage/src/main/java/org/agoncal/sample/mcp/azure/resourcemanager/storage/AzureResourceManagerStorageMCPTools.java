package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

public class AzureResourceManagerStorageMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerStorageMCPTools.class);

    private static final String AZURE_STORAGE_ACCOUNT_NAME = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String AZURE_STORAGE_ACCOUNT_KEY = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");

    @Tool(name = "creates_a_directory", description = "Creates a new directory. If the directory already exists, the operation fails")
    public ToolResponse createDirectory(@ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Creating a directory: " + directoryName);

        return ToolResponse.success();
    }
}
