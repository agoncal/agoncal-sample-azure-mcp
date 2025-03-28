package org.agoncal.sample.mcp.azure.storage.blob;

/**
 * Azure Blob Storage quickstart
 */

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.storage.models.StorageAccount;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.StorageSharedKeyCredential;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_RESOURCE_GROUP;
import static org.agoncal.sample.mcp.azure.common.Constants.AZURE_STORAGE_ACCOUNT_NAME;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AzureStorageBlobMCPToolsTest {

//    private static final String AZURE_STORAGE_ACCOUNT_NAME = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
//    private static final String AZURE_STORAGE_ACCOUNT_KEY = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");

    public static void main(String[] args) throws IOException {
        /*
         * The default credential first checks environment variables for configuration
         * If environment configuration is incomplete, it will try managed identity
         */

        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().getByResourceGroup(AZURE_RESOURCE_GROUP, AZURE_STORAGE_ACCOUNT_NAME);

        StorageSharedKeyCredential storageSharedKeyCredential = new StorageSharedKeyCredential(storageAccount.name(), storageAccount.getKeys().getFirst().value());

        // Azure SDK client builders accept the credential as a parameter
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint("https://" + AZURE_STORAGE_ACCOUNT_NAME + ".blob.core.windows.net/")
            .credential(storageSharedKeyCredential)
            .buildClient();

        // Create a unique name for the container
        String containerName = "quickstartblobs";

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = blobServiceClient.createBlobContainer(containerName);

        // Create the ./data/ directory and a file for uploading and downloading
        String localPath = "./data/";
        new File(localPath).mkdirs();
        String fileName = "quickstart" + java.util.UUID.randomUUID() + ".txt";

        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Write text to the file
        FileWriter writer = null;
        try {
            writer = new FileWriter(localPath + fileName, true);
            writer.write("Hello, World!");
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

        // Upload the blob
        blobClient.uploadFromFile(localPath + fileName);

        System.out.println("\nListing blobs...");

        // List the blob(s) in the container.
        for (BlobItem blobItem : blobContainerClient.listBlobs()) {
            System.out.println("\t" + blobItem.getName());
        }

        // Download the blob to a local file

        // Append the string "DOWNLOAD" before the .txt extension for comparison purposes
        String downloadFileName = fileName.replace(".txt", "DOWNLOAD.txt");

        System.out.println("\nDownloading blob to\n\t " + localPath + downloadFileName);

        blobClient.downloadToFile(localPath + downloadFileName);
    }
}
