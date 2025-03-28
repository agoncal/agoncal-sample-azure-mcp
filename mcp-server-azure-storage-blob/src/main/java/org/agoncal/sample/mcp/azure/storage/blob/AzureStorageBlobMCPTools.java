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

    @Tool(name = "creates_a_directory", description = "Creates a new directory in an existing storage account. If the directory already exists, the operation fails")
    public ToolResponse createDirectory(@ToolArg(name = "storage_account_name", description = "The name of the existing storage account where the directory has to be created.") String storageAccountName,
                                        @ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Creating a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        if (directory.exists()) {
            mcpLog.error("Directory " + directory.getBlobContainerName() + " already exists. Not creating it.");
        } else {
            blobServiceClient.createBlobContainer(directoryName);
            mcpLog.info("Directory " + directory.getBlobContainerName() + " has been created");
        }

        return ToolResponse.success();
    }

    @Tool(name = "creates_a_text_file", description = "Creates a new text file in a given directory in an existing storage account. If the directory does not exist, it creates it.")
    public ToolResponse createFile(@ToolArg(name = "storage_account_name", description = "The name of the existing storage account where the file has to be created.") String storageAccountName,
                                   @ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName,
                                   @ToolArg(name = "file_name", description = "The name of the file to be created in the directory. The file name cannot have spaces nor special characters. The file extension must be '.txt'.") String fileName,
                                   @ToolArg(description = "The content of the file in text format.") String content, McpLog mcpLog) {
        log.info("Creating a file: " + fileName + " in directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(storageAccountName);

        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        // Get a reference to a blob
        BlobClient file = directory.getBlobClient(fileName);

        // Convert String content to InputStream
        InputStream contentStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // Upload the blob
        file.upload(contentStream);

        mcpLog.info("File " + file.getBlobName() + " created in the directory " + directory.getBlobContainerName() + " with content size " + content.length());
        return ToolResponse.success();
    }

    @Tool(name = "reads_a_text_file", description = "Reads a text file in a given directory in an existing storage account.")
    public ToolResponse readFile(@ToolArg(name = "storage_account_name", description = "The name of the existing storage account from where the file has to be read.") String storageAccountName,
                                 @ToolArg(name = "directory_name", description = "The directory name where the file is located. The directory name cannot have spaces nor special characters.") String directoryName,
                                 @ToolArg(name = "file_name", description = "The name of the file to be read in the directory. The file name cannot have spaces nor special characters and its file extension must be '.txt'.") String fileName, McpLog mcpLog) {
        log.info("Reading a file: " + fileName + " in directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(storageAccountName);

        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        // Get a reference to a blob
        BlobClient file = directory.getBlobClient(fileName);

        // Download the content
        BinaryData data = file.downloadContent();

        mcpLog.info("File " + file.getBlobName() + " has been read from the directory " + directory.getBlobContainerName() + " with content size " + data.getLength());
        return ToolResponse.success(new TextContent(data.toString()));
    }


    @Tool(name = "deletes_a_directory", description = "Deletes a directory from an existing storage account. If the directory does not exist, the operation fails")
    public ToolResponse deleteDirectory(@ToolArg(name = "storage_account_name", description = "The name of the existing storage account from where the directory has to be deleted.") String storageAccountName,
                                        @ToolArg(name = "directory_name", description = "The directory name to be deleted. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Deleting a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);
        directory.delete();

        mcpLog.info("Directory " + directory.getBlobContainerName() + " has been deleted");
        return ToolResponse.success();
    }

    @Tool(name = "lists_files_under_a_directory", description = "Lists all the files under a directory from an existing storage account.")
    public ToolResponse listDirectories(@ToolArg(name = "storage_account_name", description = "The name of the existing storage account from where the files have to be listed.") String storageAccountName,
                                        @ToolArg(name = "directory_name", description = "The root directory name from where it lists all the files. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Listing files under a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient(storageAccountName);

        // Creates the container and return a container client object
        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        List<TextContent> files = new ArrayList<>();
        for (BlobItem file : directory.listBlobs()) {
            files.add(new TextContent(file.getName()));
        }

        mcpLog.info("Directory " + directory.getBlobContainerName() + " has " + files.size() + " files");
        return ToolResponse.success(files);
    }

    private static BlobServiceClient getBlobServiceClient(String storageAccountName) {

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().getById(storageAccountName);

        StorageSharedKeyCredential storageSharedKeyCredential = new StorageSharedKeyCredential(storageAccount.name(), storageAccount.key());

        // Azure SDK client builders accept the credential as a parameter
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint("https://" + storageAccountName + ".blob.core.windows.net/")
            .credential(storageSharedKeyCredential)
            .buildClient();
        return blobServiceClient;
    }
}
