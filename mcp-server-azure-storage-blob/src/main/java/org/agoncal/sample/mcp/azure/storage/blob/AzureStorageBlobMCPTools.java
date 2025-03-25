package org.agoncal.sample.mcp.azure.storage.blob;

import com.azure.core.util.BinaryData;
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

    private static final String AZURE_STORAGE_ACCOUNT_NAME = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String AZURE_STORAGE_ACCOUNT_KEY = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");

    @Tool(name = "creates_a_directory", description = "Creates a new directory. If the directory already exists, the operation fails")
    public ToolResponse createDirectory(@ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Creating a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient();

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

    @Tool(name = "creates_a_text_file", description = "Creates a new text file in a given directory. If the directory does not exist, it creates it.")
    public ToolResponse createFile(@ToolArg(name = "directory_name", description = "The directory name to be created. The directory name cannot have spaces nor special characters.") String directoryName,
                                   @ToolArg(name = "file_name", description = "The name of the file to be created in the directory. The file name cannot have spaces nor special characters. The file extension must be '.txt'.") String fileName,
                                   @ToolArg(description = "The content of the file in text format.") String content, McpLog mcpLog) {
        log.info("Creating a file: " + fileName + " in directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient();

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

    @Tool(name = "reads_a_text_file", description = "Reads a text file in a given directory.")
    public ToolResponse readFile(@ToolArg(name = "directory_name", description = "The directory name where the file is located. The directory name cannot have spaces nor special characters.") String directoryName,
                                 @ToolArg(name = "file_name", description = "The name of the file to be read in the directory. The file name cannot have spaces nor special characters and its file extension must be '.txt'.") String fileName, McpLog mcpLog) {
        log.info("Reading a file: " + fileName + " in directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient();

        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        // Get a reference to a blob
        BlobClient file = directory.getBlobClient(fileName);

        // Download the content
        BinaryData data = file.downloadContent();

        mcpLog.info("File " + file.getBlobName() + " has been read from the directory " + directory.getBlobContainerName() + " with content size " + data.getLength());
        return ToolResponse.success(new TextContent(data.toString()));
    }


    @Tool(name = "deletes_a_directory", description = "Deletes a directory. If the directory does not exist, the operation fails")
    public ToolResponse deleteDirectory(@ToolArg(name = "directory_name", description = "The directory name to be deleted. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Deleting a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient();

        // Creates the container and return a container client object
        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);
        directory.delete();

        mcpLog.info("Directory " + directory.getBlobContainerName() + " has been deleted");
        return ToolResponse.success();
    }

    @Tool(name = "lists_files_under_a_directory", description = "Lists all the files under a directory.")
    public ToolResponse listDirectories(@ToolArg(name = "directory_name", description = "The root directory name from where it lists all the files. The directory name cannot have spaces nor special characters.") String directoryName, McpLog mcpLog) {
        log.info("Listing files under a directory: " + directoryName);

        BlobServiceClient blobServiceClient = getBlobServiceClient();

        // Creates the container and return a container client object
        BlobContainerClient directory = blobServiceClient.getBlobContainerClient(directoryName);

        List<TextContent> files = new ArrayList<>();
        for (BlobItem file : directory.listBlobs()) {
            files.add(new TextContent(file.getName()));
        }

        mcpLog.info("Directory " + directory.getBlobContainerName() + " has " + files.size() + " files");
        return ToolResponse.success(files);
    }

    private static BlobServiceClient getBlobServiceClient() {
        StorageSharedKeyCredential storageSharedKeyCredential = new StorageSharedKeyCredential(AZURE_STORAGE_ACCOUNT_NAME, AZURE_STORAGE_ACCOUNT_KEY);

        // Azure SDK client builders accept the credential as a parameter
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint("https://" + AZURE_STORAGE_ACCOUNT_NAME + ".blob.core.windows.net/")
            .credential(storageSharedKeyCredential)
            .buildClient();
        return blobServiceClient;
    }
}
