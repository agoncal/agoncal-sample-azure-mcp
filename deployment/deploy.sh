#!/usr/bin/env bash
##############################################################################
# Dependencies:
#   * Azure CLI
#   * env.sh
##############################################################################

echo "Deploying MCP environment..."
echo "----------------------"

echo "Setting environment variables..."
echo "----------------------"
source ../env.sh

echo "Loging to Azure..."
echo "----------------------"
az login
# az account show
# az login --tenant xxxx-xxx-xxx-xxx-xxxx  --use-device-code

echo "Creating resource group..."
echo "----------------------"
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --tags system="$TAG"

echo "Deploying Storage..."
echo "----------------------"
source ./deploy-mcp-azure-storage.sh



echo "Deleting Resource Group..."
echo "----------------------"
az group delete \
  --name "$RESOURCE_GROUP" \
  --yes
