package org.agoncal.sample.mcp.azure.resourcemanager.resources;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

public class AzureResourceManagerResourcesMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerResourcesMCPTools.class);

    @Tool(name = "creates_a_resource_group", description = """
        Creates a new Resource Group in Azure.
        A Resource Group in Azure is a container that holds related resources (storage account, database, message hubs...).
        If the Resource Group already exists, the operation fails.
        """)
    public ToolResponse createResourceGroup(@ToolArg(name = "resource_group_name", description = "The name of the Resource Group to be created. The name of the Resource Group cannot have spaces and should start with the prefix 'rg-'. Add the prefix 'rg-' if it's not there.") String resourceGroupName, McpLog mcpLog) {
        log.info("Creating a resource group: " + resourceGroupName);

        AzureResourceManager azure = getAzureResourceManager();

        if (azure.resourceGroups().contain(resourceGroupName)) {
            mcpLog.error("Not creating resource group " + resourceGroupName + " because it already exists");
            return ToolResponse.error("Not creating resource group " + resourceGroupName + " because it already exists");
        } else {
            ResourceGroup resourceGroup = azure.resourceGroups().define(resourceGroupName)
                .withRegion(Region.SWEDEN_CENTRAL)
                .create();

            mcpLog.info("Resource Group " + resourceGroup.name() + " has been created");
            return ToolResponse.success();
        }
    }

    @Tool(name = "deletes_a_resource_group", description = """
        Deletes an existing Resource Group from Azure.
        If the Resource Group does not exists, the operation fails.
        """)
    public ToolResponse deleteResourceGroup(@ToolArg(name = "resource_group_name", description = "The name of the Resource Group to be deleted. .") String resourceGroupName, McpLog mcpLog) {
        log.info("Deleting a resource group: " + resourceGroupName);

        AzureResourceManager azure = getAzureResourceManager();

        if (azure.resourceGroups().contain(resourceGroupName)) {
            mcpLog.error("Not deleting resource group " + resourceGroupName + " because it does not exist");
            return ToolResponse.error("Not deleting resource group " + resourceGroupName + " because it does not exist");
        } else {
            azure.resourceGroups().deleteByName(resourceGroupName);

            mcpLog.info("Resource Group " + resourceGroupName + " has been deleted");
            return ToolResponse.success();
        }
    }

    private static AzureResourceManager getAzureResourceManager() {
        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();
        return azure;
    }
}
