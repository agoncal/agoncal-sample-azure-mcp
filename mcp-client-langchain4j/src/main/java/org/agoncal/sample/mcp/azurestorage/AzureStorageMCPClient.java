package org.agoncal.sample.mcp.azurestorage;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.List;

public class AzureStorageMCPClient {

    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    public static void main(String[] args) throws Exception {

        ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .modelName("gpt-4o-mini")
            .logRequests(true)
            .logResponses(true)
            .build();

        McpTransport transport = new StdioMcpTransport.Builder()
            .command(List.of("/Users/agoncal/.sdkman/candidates/java/23.0.1-tem/bin/java", "-jar", "/Users/agoncal/Documents/Code/AGoncal/agoncal-sample-azure-mcp/mcp-server-azure-storage/target/mcp-server-azure-storage-1.0.0-SNAPSHOT-runner.jar"))
            .logEvents(true)
            .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
            .transport(transport)
            .build();

        McpToolProvider toolProvider = McpToolProvider.builder()
            .mcpClients(List.of(mcpClient))
            .build();

        Bot bot = AiServices.builder(Bot.class)
            .chatLanguageModel(model)
            .toolProvider(toolProvider)
            .build();

        try {
            String response = bot.chat("List the files under the poem directory");
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }
    }
}
