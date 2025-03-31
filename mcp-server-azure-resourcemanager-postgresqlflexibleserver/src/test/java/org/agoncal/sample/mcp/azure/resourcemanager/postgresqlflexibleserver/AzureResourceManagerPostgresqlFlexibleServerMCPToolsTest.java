package org.agoncal.sample.mcp.azure.resourcemanager.postgresqlflexibleserver;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.Region;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.models.AzureCloud;
import com.azure.identity.DefaultAzureCredentialBuilder;
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
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_POSTGRESQL_PWD;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_POSTGRESQL_SERVER_NAME;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_POSTGRESQL_USER;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_RESOURCE_GROUP;

public class AzureResourceManagerPostgresqlFlexibleServerMCPToolsTest {

    public static void main(String[] args) {

        AzureProfile profile = new AzureProfile(AzureCloud.AZURE_PUBLIC_CLOUD);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
            .build();

        PostgreSqlManager manager = PostgreSqlManager.configure().authenticate(credential, profile);

        Server server = manager.servers()
            .define(AZURE_POSTGRESQL_SERVER_NAME)
            .withRegion(Region.US_EAST)
            .withExistingResourceGroup(AZURE_RESOURCE_GROUP)
            .withSku(new Sku().withName("Standard_D4ds_v5").withTier(SkuTier.GENERAL_PURPOSE))
            .withAuthConfig(new AuthConfig().withActiveDirectoryAuth(ActiveDirectoryAuthEnum.DISABLED)
                .withPasswordAuth(PasswordAuthEnum.ENABLED))
            .withIdentity(new UserAssignedIdentity().withType(IdentityType.NONE))
            .withDataEncryption(new DataEncryption().withType(ArmServerKeyType.SYSTEM_MANAGED))
            .withVersion(ServerVersion.ONE_SIX).withAuthConfig(null)
            .withAdministratorLogin(AZURE_POSTGRESQL_USER)
            .withAdministratorLoginPassword(AZURE_POSTGRESQL_PWD).withStorage(new Storage().withStorageSizeGB(32))
            .withHighAvailability(new HighAvailability().withMode(HighAvailabilityMode.DISABLED))
            .create();

        System.out.println("Storage account " + server.name() + " has been created in resource group " + server.resourceGroupName());
    }
}
