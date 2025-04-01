# Azure MCP Servers

In this repository, you will find some MCP Servers for Azure:

* `mcp-server-azure`: Lists the Azure regions and abbreviations suggestions.
* `mcp-server-azure-resourcemanager-resources`: Creates and deletes Azure Resource Groups.
* `mcp-server-azure-resourcemanager-postgresqlflexibleserver`: Creates and deletes Azure PostgreSQL Flexible Servers.
* `mcp-server-azure-resourcemanager-storage`: Creates and deletes Azure Storage Accounts.
* `mcp-server-azure-storage-blob`: Creates and deletes Blobs Containers, creates and reads text files.

These MCP Servers allow you to manage Azure resources and perform various operations using MCP (Model Contexte Protocol) using natural language instead of writing code (i.e. Azure SDK), or using Azure CLI commands or Bicep/Terraform templates.
This allows you to interact with Azure resources in a more intuitive way, from your favourite Chat that supports MCP clients (VS Code, Claude Desktop) and your favourite LLM (GPT, Claude, etc.).

In the screenshot below, you can see how to use GH Copilot Chat within VS Code to create an Azure Resource Group and a Storage Account:

![mcp.png](mcp.png)
