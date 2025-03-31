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
import com.azure.storage.common.StorageSharedKeyCredential;

import java.io.IOException;

public class ReadingFile {

    private static final String AZURE_RESOURCE_GROUP = "rg-mcpazure";
    private static final String AZURE_STORAGE_ACCOUNT_NAME = "stmcpazure";
    private static final String AZURE_STORAGE_CONTAINER_NAME = "poems";
    private static final String FILE_NAME = "city_of_paris.txt";

    public static void main(String[] args) throws IOException {

        // Connects to Azure Storage Account
        AzureResourceManager azure = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();

        StorageAccount storageAccount = azure.storageAccounts().getByResourceGroup(AZURE_RESOURCE_GROUP, AZURE_STORAGE_ACCOUNT_NAME);

        StorageSharedKeyCredential storageSharedKeyCredential = new StorageSharedKeyCredential(storageAccount.name(), storageAccount.getKeys().getFirst().value());

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint("https://" + AZURE_STORAGE_ACCOUNT_NAME + ".blob.core.windows.net/")
            .credential(storageSharedKeyCredential)
            .buildClient();


        // Gets a reference to the 'poems' container
        BlobContainerClient container = blobServiceClient.getBlobContainerClient(AZURE_STORAGE_CONTAINER_NAME);

        // Gets a reference to the 'city_of_paris.txt' file
        BlobClient blobClient = container.getBlobClient(FILE_NAME);

        // Downloads the file to a local file
        blobClient.downloadToFile("downloaded_" + FILE_NAME);
    }
}
