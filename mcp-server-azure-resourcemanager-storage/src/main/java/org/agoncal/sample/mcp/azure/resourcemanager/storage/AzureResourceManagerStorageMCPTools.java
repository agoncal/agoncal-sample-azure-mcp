package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.resourcemanager.storage.models.StorageAccountSkuType;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

public class AzureResourceManagerStorageMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerStorageMCPTools.class);

    @Tool(name = "creates_a_storage_account", description = "Creates a new Storage Account in an existing Azure Resource Group. If the Storage Account already exists, the operation fails.")
    public ToolResponse createStorageAccount(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                             @ToolArg(name = "storage_account_name", description = "The name of the Storage Account to be created. A Storage Account in Azure provides a unique namespace to store and access Azure Storage data objects, such as blobs, file shares, queues, tables, and disks. It allows you to manage data storage. The name of the Storage Account cannot have spaces not special characters, and should start with the prefix 'st'.") String storageAccountName,
                                             McpLog mcpLog) {
        log.info("Creating a storage account: " + storageAccountName);

        AzureResourceManager azure = getAzureResourceManager();

        if (!azure.storageAccounts().checkNameAvailability(storageAccountName).isAvailable()) {
            mcpLog.error("Not creating storage account " + storageAccountName + " because it already exists");
            return ToolResponse.error("Not creating storage account " + storageAccountName + " because it already exists");
        } else {

            StorageAccount storageAccount = azure.storageAccounts().define(storageAccountName)
                .withRegion(Region.US_EAST)
                .withExistingResourceGroup(resourceGroupName)
                .withAccessFromAllNetworks()
                .withSku(StorageAccountSkuType.STANDARD_RAGRS)
                .withGeneralPurposeAccountKindV2()
                .create();

            mcpLog.info("Storage Account " + storageAccount.name() + " has been created");
        }
        return ToolResponse.success();
    }

    @Tool(name = "deletes_a_storage_account", description = "Deletes an existing Storage Account. If the Storage Account does not exists, the operation fails")
    public ToolResponse deleteStorageAccount(@ToolArg(name = "storage_account_name", description = "The name of the Storage Account to be deleted.") String storageAccountName,
                                             McpLog mcpLog) {
        log.info("Deleting a storage account: " + storageAccountName);

        AzureResourceManager azure = getAzureResourceManager();

        azure.storageAccounts().deleteById(storageAccountName);

        mcpLog.info("Storage Account " + storageAccountName + " has been deleted");
        return ToolResponse.success();
    }

    private static AzureResourceManager getAzureResourceManager() {
        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();
        return azure;
    }
}
