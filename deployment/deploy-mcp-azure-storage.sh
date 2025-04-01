#!/usr/bin/env bash
##############################################################################
# Dependencies:
#   * Azure CLI
#   * env.sh
##############################################################################


echo "Creating the Storage Account..."
echo "----------------------"
az storage account create \
  --resource-group "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --tags system="$TAG" \
  --name "$STORAGE_ACCOUNT" \
  --allow-blob-public-access "true" \
  --allow-shared-key-access "true" \
  --public-network-access "Enabled" \
  --sku "Standard_RAGRS" \
  --kind "StorageV2"


AZURE_STORAGE_ACCOUNT_KEY=$(az storage account keys list \
  --resource-group "$RESOURCE_GROUP" \
  --account-name "$STORAGE_ACCOUNT" \
  --query "[0].value" \
  --output tsv)

echo "AZURE_STORAGE_ACCOUNT_KEY=$AZURE_STORAGE_ACCOUNT_KEY"

STORAGE_ACCOUNT_CONNECTION_STRING=$(az storage account show-connection-string \
  --resource-group "$RESOURCE_GROUP" \
  --name "$STORAGE_ACCOUNT" \
  --output tsv)

echo "STORAGE_ACCOUNT_CONNECTION_STRING=$STORAGE_ACCOUNT_CONNECTION_STRING"

STORAGE_ACCOUNT_ID=$(az storage account show \
  --resource-group "$RESOURCE_GROUP" \
  --name "$STORAGE_ACCOUNT" \
  --query id)

echo "STORAGE_ACCOUNT_ID=$STORAGE_ACCOUNT_ID"

echo "Creating the Container..."
echo "----------------------"
az storage container create \
    --account-name "$STORAGE_ACCOUNT" \
    --name "poems" \
    --account-key "$STORAGE_ACCOUNT_KEY" \
    --auth-mode "key"


echo "Deleting the Storage Account..."
echo "----------------------"
az storage account delete \
  --resource-group "$RESOURCE_GROUP" \
  --name "$STORAGE_ACCOUNT" \
  --yes
