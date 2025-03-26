package org.agoncal.sample.mcp.azure.resourcemanager.resources;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.resources.ResourceManager;
import com.azure.resourcemanager.resources.models.ResourceGroup;
import io.quarkiverse.mcp.server.Content;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AzureResourceManagerResourcesMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerResourcesMCPTools.class);

    private static final String AZURE_STORAGE_ACCOUNT_NAME = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String AZURE_STORAGE_ACCOUNT_KEY = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");

    @Tool(name = "creates_a_resource_group", description = "Creates a new directory. If the directory already exists, the operation fails")
    public ToolResponse createResourceGroup(@ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Creating a directory: " + directoryName);

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();
        ResourceManager manager = ResourceManager
            .authenticate(credential, profile)
            .withDefaultSubscription();

        ResourceGroup resourceGroup = manager.resourceGroups().define("myResourceGroup")
            .withRegion(Region.US_EAST)
            .create();

        return ToolResponse.success();
    }

    @Tool(name = "lists_all_the_regions", description = "Creates a new directory. If the directory already exists, the operation fails")
    public ToolResponse listRegions(@ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Creating a directory: " + directoryName);

        List<Content> content = new ArrayList<>();
        Collection<Region> regions = Region.values();
        for (Iterator<Region> iterator = regions.iterator(); iterator.hasNext(); ) {
            Region region = iterator.next();
            content.add(new TextContent(region.name()));
        }

        return ToolResponse.success(content);
    }
}
