package org.agoncal.sample.mcp.azure.resourcemanager.storage;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.storage.StorageManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.resourcemanager.storage.models.StorageAccountSkuType;
import org.jboss.logging.Logger;

public class AzureResourceManagerStorageMCPToolsTest {

    private static final Logger log = Logger.getLogger(AzureResourceManagerStorageMCPToolsTest.class);

    public static void main(String[] args) {

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);

        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        StorageManager storageManager = StorageManager
            .authenticate(credential, profile);

        StorageAccount storageAccount = storageManager.storageAccounts().define("st-mcpazure-storage")
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup("rg-mcpazure-storage")
            .withAccessFromAllNetworks()
            .withSku(StorageAccountSkuType.STANDARD_RAGRS)
            .withGeneralPurposeAccountKindV2()
            .create();

        System.out.println("Storage account " + storageAccount.name() + " has been created");
    }
}
