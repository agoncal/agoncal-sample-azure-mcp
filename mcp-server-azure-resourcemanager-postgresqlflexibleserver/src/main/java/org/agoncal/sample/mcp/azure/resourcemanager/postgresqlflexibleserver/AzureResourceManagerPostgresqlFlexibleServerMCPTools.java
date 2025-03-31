package org.agoncal.sample.mcp.azure.resourcemanager.postgresqlflexibleserver;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.postgresqlflexibleserver.PostgreSqlManager;
import com.azure.resourcemanager.postgresqlflexibleserver.models.ActiveDirectoryAuthEnum;
import com.azure.resourcemanager.postgresqlflexibleserver.models.ArmServerKeyType;
import com.azure.resourcemanager.postgresqlflexibleserver.models.AuthConfig;
import com.azure.resourcemanager.postgresqlflexibleserver.models.DataEncryption;
import com.azure.resourcemanager.postgresqlflexibleserver.models.HighAvailability;
import com.azure.resourcemanager.postgresqlflexibleserver.models.HighAvailabilityMode;
import com.azure.resourcemanager.postgresqlflexibleserver.models.IdentityType;
import com.azure.resourcemanager.postgresqlflexibleserver.models.PasswordAuthEnum;
import com.azure.resourcemanager.postgresqlflexibleserver.models.Server;
import com.azure.resourcemanager.postgresqlflexibleserver.models.ServerVersion;
import com.azure.resourcemanager.postgresqlflexibleserver.models.Sku;
import com.azure.resourcemanager.postgresqlflexibleserver.models.SkuTier;
import com.azure.resourcemanager.postgresqlflexibleserver.models.Storage;
import com.azure.resourcemanager.postgresqlflexibleserver.models.UserAssignedIdentity;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

public class AzureResourceManagerPostgresqlFlexibleServerMCPTools {

    private static final Logger log = Logger.getLogger(AzureResourceManagerPostgresqlFlexibleServerMCPTools.class);

    @Tool(name = "creates_a_postgresql_flexible_server", description = "Creates a new Postgresql Flexible Server in an existing Azure Resource Group. If the Postgresql Flexible Server already exists, the operation succeeds silently.")
    public ToolResponse createPostgresqlFlexibleServer(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                                       @ToolArg(name = "postgresql_flexible_server_name", description = "The name of the Postgresql Flexible Server to be created. A Postgresql Flexible Server in Azure is a fully managed database service that provides more control and flexibility over database configuration parameters. The name of the database cannot have spaces, and should start with the prefix 'psql-'.") String postgresqlFlexibleServerName,
                                                       McpLog mcpLog) {
        log.info("Creating a postgresql flexible server: " + postgresqlFlexibleServerName);

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        PostgreSqlManager manager = PostgreSqlManager.configure().authenticate(credential, profile);

        Server server = manager.servers()
            .define(postgresqlFlexibleServerName)
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup(resourceGroupName)
            .withSku(new Sku().withName("Standard_D4ds_v5").withTier(SkuTier.GENERAL_PURPOSE))
            .withAuthConfig(new AuthConfig().withActiveDirectoryAuth(ActiveDirectoryAuthEnum.DISABLED)
                .withPasswordAuth(PasswordAuthEnum.ENABLED))
            .withIdentity(new UserAssignedIdentity().withType(IdentityType.NONE))
            .withDataEncryption(new DataEncryption().withType(ArmServerKeyType.SYSTEM_MANAGED))
            .withVersion(ServerVersion.ONE_SIX).withAuthConfig(null)
            .withAdministratorLogin("<user-name>")
            .withAdministratorLoginPassword("<password>").withStorage(new Storage().withStorageSizeGB(32))
            .withHighAvailability(new HighAvailability().withMode(HighAvailabilityMode.DISABLED))
            .create();

        mcpLog.info("Postgresql flexible server " + server.name() + " has been created");
        return ToolResponse.success();
    }

    @Tool(name = "deletes_a_postgresql_flexible_server", description = "Deletes an existing Postgresql Flexible Server from Azure. If the Database does not exists, the operation fails")
    public ToolResponse deletePostgresqlFlexibleServer(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                                       @ToolArg(name = "postgresql_flexible_server_name", description = "The name of the Postgresql Flexible Server to be deleted.") String postgresqlFlexibleServerName,
                                                       McpLog mcpLog) {
        log.info("Deleting a postgresql flexible server: : " + postgresqlFlexibleServerName);

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        PostgreSqlManager manager = PostgreSqlManager.configure().authenticate(credential, profile);

        manager.servers().deleteByResourceGroup(resourceGroupName, postgresqlFlexibleServerName);

        mcpLog.info("Postgresql flexible server " + postgresqlFlexibleServerName + " has been deleted");
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
