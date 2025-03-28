package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.resourcemanager.storage.models.StorageAccountSkuType;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_RESOURCE_GROUP;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_STORAGE_ACCOUNT_NAME;

public class AzureResourceManagerStorageMCPToolsTest {

    public static void main(String[] args) {

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().define(AZURE_STORAGE_ACCOUNT_NAME)
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup(AZURE_RESOURCE_GROUP)
            .withAccessFromAllNetworks()
            .withSku(StorageAccountSkuType.STANDARD_RAGRS)
            .withGeneralPurposeAccountKindV2()
            .create();

        System.out.println("Storage account " + storageAccount.name() + " has been created in resource group " + storageAccount.resourceGroupName());
    }
}
