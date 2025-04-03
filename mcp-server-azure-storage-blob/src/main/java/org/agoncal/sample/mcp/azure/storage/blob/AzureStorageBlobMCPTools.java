package org.agoncal.sample.mcp.azure.storage.blob;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.util.BinaryData;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.StorageSharedKeyCredential;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AzureStorageBlobMCPTools {

    private static final Logger log = Logger.getLogger(AzureStorageBlobMCPTools.class);

    @Tool(name = "creates_a_blob_container", description = """
        Creates a new Blob Container in an existing Storage Account in an existing Azure Resource Group.
        If the Blob Container already exists, the operation succeeds silently.
        """)
    public ToolResponse createBlobContainer(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                            @ToolArg(name = "storage_account_name", description = "The name of the existing Storage Account.") String storageAccountName,
                                            @ToolArg(name = "blob_container_name", description = "The name of the Blob Container to be created. The Blob Container name cannot have spaces.") String blobContainerName,
                                            McpLog mcpLog) {
        log.info("Creating a blob container: " + blobContainerName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(resourceGroupName, storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(blobContainerName);

        if (blobContainer.exists()) {
            mcpLog.error("Not creating Blob Container " + storageAccountName + " because it already exists");
            return ToolResponse.error("Not creating Blob Container " + storageAccountName + " because it already exists");
        } else {
            blobServiceClient.createBlobContainer(blobContainerName);
            mcpLog.info("Blob Container " + blobContainer.getBlobContainerName() + " has been created in storage account " + blobContainer.getAccountName());
            return ToolResponse.success();
        }
    }

    @Tool(name = "creates_a_text_file", description = """
        Creates a new text file in a given Blob Container.
        If the Blob Container does not exist, it creates it before creating the file.
        """)
    public ToolResponse createFile(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                   @ToolArg(name = "storage_account_name", description = "The name of the existing Storage Account.") String storageAccountName,
                                   @ToolArg(name = "blob_container_name", description = "The name of the Blob Container where the text file has to be created. The Blob Container name cannot have spaces.") String blobContainerName,
                                   @ToolArg(name = "file_name", description = "The name of the file to be created in the Blob Container. The file name cannot have spaces nor special characters. The file extension must be '.txt'.") String fileName,
                                   @ToolArg(description = "The content of the file in text format.") String content,
                                   McpLog mcpLog) {
        log.info("Creating a file: " + fileName + " in blob container: " + blobContainerName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(resourceGroupName, storageAccountName);

        BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(blobContainerName);

        // Get a reference to a blob
        BlobClient file = blobContainer.getBlobClient(fileName);

        // Convert String content to InputStream
        InputStream contentStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Upload the blob
        file.upload(contentStream);

        mcpLog.info("File " + file.getBlobName() + " created in the blobContainer " + blobContainer.getBlobContainerName() + " in storage account " + blobContainer.getAccountName() + " with content size " + content.length());
        return ToolResponse.success();
    }

    @Tool(name = "reads_a_text_file", description = """
        Reads a text file in a given blobContainer in an existing storage account in an existing resource group.
        """)
    public ToolResponse readFile(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                 @ToolArg(name = "storage_account_name", description = "The name of the existing Storage Account.") String storageAccountName,
                                 @ToolArg(name = "blob_container_name", description = "The name of the Blob Container where the text is located.") String blobContainerName,
                                 @ToolArg(name = "file_name", description = "The name of the file to be read in the Blob Container. The file name cannot have spaces nor special characters and its file extension must be '.txt'.") String fileName, McpLog mcpLog) {
        log.info("Reading a file: " + fileName + " in blobContainer: " + blobContainerName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(resourceGroupName, storageAccountName);

        BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(blobContainerName);

        // Get a reference to a blob
        BlobClient file = blobContainer.getBlobClient(fileName);

        // Download the content
        BinaryData data = file.downloadContent();

        mcpLog.info("File " + file.getBlobName() + " has been read from the blobContainer " + blobContainer.getBlobContainerName() + " with content size " + data.getLength());
        return ToolResponse.success(new TextContent(data.toString()));
    }


    @Tool(name = "deletes_a_blob_container", description = """
        Deletes a blob container from an existing storage account from an existing resource group.
        If the blobContainer does not exist, the operation fails.
        """)
    public ToolResponse deleteBlobContainer(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                            @ToolArg(name = "storage_account_name", description = "The name of the existing Storage Account.") String storageAccountName,
                                            @ToolArg(name = "blob_container_name", description = "The name of the blob Container name to be deleted..") String blobContainerName,
                                            McpLog mcpLog) {
        log.info("Deleting a blob container: " + blobContainerName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(resourceGroupName, storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(blobContainerName);
        blobContainer.delete();

        mcpLog.info("BlobContainer " + blobContainer.getBlobContainerName() + " has been deleted");
        return ToolResponse.success();
    }

    @Tool(name = "lists_files_under_a_blob_container", description = """
        Lists all the files under a blob container from an existing storage account from an existing resource group.
        """)
    public ToolResponse listDirectories(@ToolArg(name = "resource_group_name", description = "The name of the existing Azure Resource Group.") String resourceGroupName,
                                        @ToolArg(name = "storage_account_name", description = "The name of the existing Storage Account.") String storageAccountName,
                                        @ToolArg(name = "blob_container_name", description = "The name of the Blob Container name from where all the files are listed.") String blobContainerName,
                                        McpLog mcpLog) {
        log.info("Listing files under a blob container: " + blobContainerName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(resourceGroupName, storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient blobContainer = blobServiceClient.getBlobContainerClient(blobContainerName);

        List<TextContent> files = new ArrayList<>();
        for (BlobItem file : blobContainer.listBlobs()) {
            files.add(new TextContent(file.getName()));
        }

        mcpLog.info("BlobContainer " + blobContainer.getBlobContainerName() + " has " + files.size() + " files");
        return ToolResponse.success(files);
    }

    private static BlobServiceClient getBlobServiceClient(String resourceGroupName, String storageAccountName) {

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().getByResourceGroup(resourceGroupName, storageAccountName);

        StorageSharedKeyCredential storageSharedKeyCredential = new StorageSharedKeyCredential(storageAccount.name(), storageAccount.getKeys().getFirst().value());

        // Azure SDK client builders accept the credential as a parameter
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint("https://" + storageAccountName + ".blob.core.windows.net/")
            .credential(storageSharedKeyCredential)
            .buildClient();

        return blobServiceClient;
    }
}
