package org.agoncal.sample.mcp.azure.resourcemanager.resources;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_RESOURCE_GROUP;

public class AzureResourceManagerResourcesMCPToolsTest {

    public static void main(String[] args) {

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        ResourceGroup resourceGroup = azure.resourceGroups().define(AZURE_RESOURCE_GROUP)
            .withRegion(Region.SWEDEN_CENTRAL)
            .create();

        System.out.println("Resource Group " + resourceGroup.name() + " has been created");
    }
}
