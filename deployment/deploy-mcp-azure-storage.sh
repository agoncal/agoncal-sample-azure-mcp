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
  --name "$STORAGE_ACCOUNT"


echo "Deleting the Storage Account..."
echo "----------------------"
az storage account delete \
  --resource-group "$RESOURCE_GROUP" \
  --name "$STORAGE_ACCOUNT" \
  --yes
