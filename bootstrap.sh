#!/usr/bin/env bash

### #################################################################
### This script is used to bootstrap the environment for the workshop
### #################################################################


echo "### Bootstraps the Quarkus App"
mvn io.quarkus:quarkus-maven-plugin:3.19.3:create \
    -DplatformVersion=3.19.3 \
    -DprojectGroupId=org.agoncal.sample.mcp \
    -DprojectArtifactId=mcp-azure-storage \
    -DprojectName="MCP :: Azure Storage" \
    -DjavaVersion=21 \
    -DclassName="org.agoncal.sample.mcp.azurestorage.AzureStorageResource" \
    -Dpath="/azurestorage" \
    -Dextensions="resteasy, resteasy-jsonb, hibernate-orm-panache, jdbc-postgresql"
