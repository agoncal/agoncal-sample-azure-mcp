package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.storage.StorageManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

public class AzureResourceManagerStorageMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerStorageMCPTools.class);

    @Tool(name = "creates_a_storage_account", description = "Creates a new storage account in an existing resource group. If the storage account already exists, the operation fails")
    public ToolResponse createStorageAccount(@ToolArg(name = "storage_account_name", description = "The name of the storage account to be created. A storage account in Azure provides a unique namespace to store and access Azure Storage data objects, such as blobs, file shares, queues, tables, and disks. It allows you to manage data storage . The name of the storage account cannot have spaces not special characters, and should start with the prefix 'st'.") String storageAccountName,
                                             @ToolArg(name = "resource_group_name", description = "The name of the existing resource group where the storage account is located.") String resourceGroupName,
                                             McpLog mcpLog) {
        log.info("Creating a storage account: " + storageAccountName);

        StorageManager storageManager = getStorageManager();

        StorageAccount storageAccount = storageManager.storageAccounts().define(storageAccountName)
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup(resourceGroupName)
            .create();

        mcpLog.info("Storage Account " + storageAccount.name() + " has been created");
        return ToolResponse.success();
    }

    @Tool(name = "deletes_a_storage_account", description = "Deletes an existing storage account. If the storage account does not exists, the operation fails")
    public ToolResponse deleteStorageAccount(@ToolArg(name = "storage_account_name", description = "The name of the storage account to be deleted. The name of the storage account cannot have spaces nor special characters, and should start with the prefix 'st'.") String storageAccountName,
                                             McpLog mcpLog) {
        log.info("Creating a storage account: " + storageAccountName);


        StorageManager storageManager = getStorageManager();
        storageManager.storageAccounts().deleteById(storageAccountName);

        mcpLog.info("Storage Account " + storageAccountName + " has been deleted");
        return ToolResponse.success();
    }

    private static StorageManager getStorageManager() {
        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);

        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        StorageManager storageManager = StorageManager
            .authenticate(credential, profile);

        return storageManager;
    }
}
