package org.agoncal.sample.mcp.azure.resourcemanager.resources;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.resources.ResourceManager;
import com.azure.resourcemanager.resources.models.ResourceGroup;

import java.time.Instant;

public class AzureResourceManagerResourcesMCPToolsTest {

    public static void main(String[] args) {

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        ResourceManager manager = ResourceManager
            .authenticate(credential, profile)
            .withDefaultSubscription();

        ResourceGroup resourceGroup = manager.resourceGroups().define("rg-mcpazure" + Instant.now().toEpochMilli())
            .withRegion(Region.US_EAST)
            .create();

        System.out.println("Resource Group " + resourceGroup.name() + " has been created");
    }
}
