package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.resourcemanager.storage.models.StorageAccountSkuType;
import org.jboss.logging.Logger;

public class AzureResourceManagerStorageMCPToolsTest {

    private static final Logger log = Logger.getLogger(AzureResourceManagerStorageMCPToolsTest.class);

    public static void main(String[] args) {

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().define("stmcpazureantoniomanug")
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup("rg-mcpazure-storage")
            .withAccessFromAllNetworks()
            .withSku(StorageAccountSkuType.STANDARD_RAGRS)
            .withGeneralPurposeAccountKindV2()
            .create();

        System.out.println("Storage account " + storageAccount.name() + " has been created");
    }
}
