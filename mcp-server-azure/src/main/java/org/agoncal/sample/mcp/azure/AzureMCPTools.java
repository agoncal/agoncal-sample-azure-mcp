package org.agoncal.sample.mcp.azure;

import com.azure.core.management.Region;
import io.quarkiverse.mcp.server.Content;
import io.quarkiverse.mcp.server.McpLog;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AzureMCPTools {

    private static final Logger log = Logger.getLogger(AzureMCPTools.class);

    @Tool(name = "lists_all_the_regions", description = """
        Lists all the available regions in Azure.
        A region in Azure is a geographical area containing one or more data centers that are in close proximity and networked together with a low-latency network.
        """)
    public ToolResponse listRegions(McpLog mcpLog) {
        log.info("Listing all the regions");

        List<Content> content = new ArrayList<>();
        Collection<Region> regions = Region.values();
        for (Iterator<Region> iterator = regions.iterator(); iterator.hasNext(); ) {
            Region region = iterator.next();
            content.add(new TextContent(region.name()));
        }

        mcpLog.info("Returning " + regions.size() + " regions");
        return ToolResponse.success(content);
    }

    @Tool(name = "lists_all_the_resource_abbreviation", description = """
        Returns the abbreviations for many of the resources in Azure.
        The following list has abbreviations mapped to resource and abbreviation
        """)
    public ToolResponse listAbbreviations(McpLog mcpLog) {
        log.info("Listing all the known abbreviations");

        List<Content> content = new ArrayList<>();

        content.add(new TextContent(new Pair("AI Search", "srch").toString()));
        content.add(new TextContent(new Pair("Azure AI services", "ais").toString()));
        content.add(new TextContent(new Pair("Azure AI Foundry hub", "hub").toString()));
        content.add(new TextContent(new Pair("Azure AI Foundry project", "proj").toString()));
        content.add(new TextContent(new Pair("Azure AI Video Indexer", "avi").toString()));
        content.add(new TextContent(new Pair("Azure Machine Learning workspace", "mlw").toString()));
        content.add(new TextContent(new Pair("Azure OpenAI Service", "oai").toString()));
        content.add(new TextContent(new Pair("Bot service", "bot").toString()));
        content.add(new TextContent(new Pair("Computer vision", "cv").toString()));
        content.add(new TextContent(new Pair("Content moderator", "cm").toString()));
        content.add(new TextContent(new Pair("Content safety", "cs").toString()));
        content.add(new TextContent(new Pair("Custom vision (prediction)", "cstv").toString()));
        content.add(new TextContent(new Pair("Custom vision (training)", "cstvt").toString()));
        content.add(new TextContent(new Pair("Document intelligence", "di").toString()));
        content.add(new TextContent(new Pair("Face API", "face").toString()));
        content.add(new TextContent(new Pair("Health Insights", "hi").toString()));
        content.add(new TextContent(new Pair("Immersive reader", "ir").toString()));
        content.add(new TextContent(new Pair("Language service", "lang").toString()));
        content.add(new TextContent(new Pair("Speech service", "spch").toString()));
        content.add(new TextContent(new Pair("Translator", "trsl").toString()));
        content.add(new TextContent(new Pair("Azure Analysis Services server", "as").toString()));
        content.add(new TextContent(new Pair("Azure Databricks workspace", "dbw").toString()));
        content.add(new TextContent(new Pair("Azure Data Explorer cluster", "dec").toString()));
        content.add(new TextContent(new Pair("Azure Data Explorer cluster database", "dedb").toString()));
        content.add(new TextContent(new Pair("Azure Data Factory", "adf").toString()));
        content.add(new TextContent(new Pair("Azure Digital Twin instance", "dt").toString()));
        content.add(new TextContent(new Pair("Azure Stream Analytics", "asa").toString()));
        content.add(new TextContent(new Pair("Azure Synapse Analytics private link hub", "synplh").toString()));
        content.add(new TextContent(new Pair("Azure Synapse Analytics SQL Dedicated Pool", "syndp").toString()));
        content.add(new TextContent(new Pair("Azure Synapse Analytics Spark Pool", "synsp").toString()));
        content.add(new TextContent(new Pair("Azure Synapse Analytics workspaces", "synw").toString()));
        content.add(new TextContent(new Pair("Data Lake Store account", "dls").toString()));
        content.add(new TextContent(new Pair("Data Lake Analytics account", "dla").toString()));
        content.add(new TextContent(new Pair("Event Hubs namespace", "evhns").toString()));
        content.add(new TextContent(new Pair("Event hub", "evh").toString()));
        content.add(new TextContent(new Pair("Event Grid domain", "evgd").toString()));
        content.add(new TextContent(new Pair("Event Grid subscriptions", "evgs").toString()));
        content.add(new TextContent(new Pair("Event Grid topic", "evgt").toString()));
        content.add(new TextContent(new Pair("Event Grid system topic", "egst").toString()));
        content.add(new TextContent(new Pair("HDInsight - Hadoop cluster", "hadoop").toString()));
        content.add(new TextContent(new Pair("HDInsight - HBase cluster", "hbase").toString()));
        content.add(new TextContent(new Pair("HDInsight - Kafka cluster", "kafka").toString()));
        content.add(new TextContent(new Pair("HDInsight - Spark cluster", "spark").toString()));
        content.add(new TextContent(new Pair("HDInsight - Storm cluster", "storm").toString()));
        content.add(new TextContent(new Pair("HDInsight - ML Services cluster", "mls").toString()));
        content.add(new TextContent(new Pair("IoT hub", "iot").toString()));
        content.add(new TextContent(new Pair("Provisioning services", "provs").toString()));
        content.add(new TextContent(new Pair("Provisioning services certificate", "pcert").toString()));
        content.add(new TextContent(new Pair("Power BI Embedded", "pbi").toString()));
        content.add(new TextContent(new Pair("Time Series Insights environment", "tsi").toString()));
        content.add(new TextContent(new Pair("App Service environment", "ase").toString()));
        content.add(new TextContent(new Pair("App Service plan", "asp").toString()));
        content.add(new TextContent(new Pair("Azure Load Testing instance", "lt").toString()));
        content.add(new TextContent(new Pair("Availability set", "avail").toString()));
        content.add(new TextContent(new Pair("Azure Arc enabled server", "arcs").toString()));
        content.add(new TextContent(new Pair("Azure Arc enabled Kubernetes cluster", "arck").toString()));
        content.add(new TextContent(new Pair("Azure Arc private link scope", "pls").toString()));
        content.add(new TextContent(new Pair("Azure Arc gateway", "arcgw").toString()));
        content.add(new TextContent(new Pair("Batch accounts", "ba").toString()));
        content.add(new TextContent(new Pair("Cloud service", "cld").toString()));
        content.add(new TextContent(new Pair("Communication Services", "acs").toString()));
        content.add(new TextContent(new Pair("Disk encryption set", "des").toString()));
        content.add(new TextContent(new Pair("Function app", "func").toString()));
        content.add(new TextContent(new Pair("Gallery", "gal").toString()));
        content.add(new TextContent(new Pair("Hosting environment", "host").toString()));
        content.add(new TextContent(new Pair("Image template", "it").toString()));
        content.add(new TextContent(new Pair("Managed disk (OS)", "osdisk").toString()));
        content.add(new TextContent(new Pair("Managed disk (data)", "disk").toString()));
        content.add(new TextContent(new Pair("Notification Hubs", "ntf").toString()));
        content.add(new TextContent(new Pair("Notification Hubs namespace", "ntfns").toString()));
        content.add(new TextContent(new Pair("Proximity placement group", "ppg").toString()));
        content.add(new TextContent(new Pair("Restore point collection", "rpc").toString()));
        content.add(new TextContent(new Pair("Snapshot", "snap").toString()));
        content.add(new TextContent(new Pair("Static web app", "stapp").toString()));
        content.add(new TextContent(new Pair("Virtual machine", "vm").toString()));
        content.add(new TextContent(new Pair("Virtual machine scale set", "vmss").toString()));
        content.add(new TextContent(new Pair("Virtual machine maintenance configuration", "mc").toString()));
        content.add(new TextContent(new Pair("VM storage account", "stvm").toString()));
        content.add(new TextContent(new Pair("Web app", "app").toString()));
        content.add(new TextContent(new Pair("AKS cluster", "aks").toString()));
        content.add(new TextContent(new Pair("AKS system node pool", "npsystem").toString()));
        content.add(new TextContent(new Pair("AKS user node pool", "np").toString()));
        content.add(new TextContent(new Pair("Container apps", "ca").toString()));
        content.add(new TextContent(new Pair("Container apps environment", "cae").toString()));
        content.add(new TextContent(new Pair("Container registry", "cr").toString()));
        content.add(new TextContent(new Pair("Container instance", "ci").toString()));
        content.add(new TextContent(new Pair("Service Fabric cluster", "sf").toString()));
        content.add(new TextContent(new Pair("Service Fabric managed cluster", "sfmc").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB database", "cosmos").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB for Apache Cassandra account", "coscas").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB for MongoDB account", "cosmon").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB for NoSQL account", "cosno").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB for Table account", "costab").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB for Apache Gremlin account", "cosgrm").toString()));
        content.add(new TextContent(new Pair("Azure Cosmos DB PostgreSQL cluster", "cospos").toString()));
        content.add(new TextContent(new Pair("Azure Cache for Redis instance", "redis").toString()));
        content.add(new TextContent(new Pair("Azure SQL Database server", "sql").toString()));
        content.add(new TextContent(new Pair("Azure SQL database", "sqldb").toString()));
        content.add(new TextContent(new Pair("Azure SQL Elastic Job agent", "sqlja").toString()));
        content.add(new TextContent(new Pair("Azure SQL Elastic Pool", "sqlep").toString()));
        content.add(new TextContent(new Pair("MariaDB server", "maria").toString()));
        content.add(new TextContent(new Pair("MariaDB database", "mariadb").toString()));
        content.add(new TextContent(new Pair("MySQL database", "mysql").toString()));
        content.add(new TextContent(new Pair("PostgreSQL database", "psql").toString()));
        content.add(new TextContent(new Pair("SQL Server Stretch Database", "sqlstrdb").toString()));
        content.add(new TextContent(new Pair("SQL Managed Instance", "sqlmi").toString()));
        content.add(new TextContent(new Pair("App Configuration store", "appcs").toString()));
        content.add(new TextContent(new Pair("Maps account", "map").toString()));
        content.add(new TextContent(new Pair("SignalR", "sigr").toString()));
        content.add(new TextContent(new Pair("WebPubSub", "wps").toString()));
        content.add(new TextContent(new Pair("Azure Managed Grafana", "amg").toString()));
        content.add(new TextContent(new Pair("API management service instance", "apim").toString()));
        content.add(new TextContent(new Pair("Integration account", "ia").toString()));
        content.add(new TextContent(new Pair("Logic app", "logic").toString()));
        content.add(new TextContent(new Pair("Service Bus namespace", "sbns").toString()));
        content.add(new TextContent(new Pair("Service Bus queue", "sbq").toString()));
        content.add(new TextContent(new Pair("Service Bus topic", "sbt").toString()));
        content.add(new TextContent(new Pair("Service Bus topic subscription", "sbts").toString()));
        content.add(new TextContent(new Pair("Automation account", "aa").toString()));
        content.add(new TextContent(new Pair("Azure Policy definition", "< descriptive >").toString()));
        content.add(new TextContent(new Pair("Application Insights", "appi").toString()));
        content.add(new TextContent(new Pair("Azure Monitor action group", "ag").toString()));
        content.add(new TextContent(new Pair("Azure Monitor data collection rule", "dcr").toString()));
        content.add(new TextContent(new Pair("Azure Monitor alert processing rule", "apr").toString()));
        content.add(new TextContent(new Pair("Blueprint (planned for deprecation)", "bp").toString()));
        content.add(new TextContent(new Pair("Blueprint assignment (planned for deprecation)", "bpa").toString()));
        content.add(new TextContent(new Pair("Data collection endpoint", "dce").toString()));
        content.add(new TextContent(new Pair("Log Analytics workspace", "log").toString()));
        content.add(new TextContent(new Pair("Log Analytics query packs", "pack").toString()));
        content.add(new TextContent(new Pair("Management group", "mg").toString()));
        content.add(new TextContent(new Pair("Microsoft Purview instance", "pview").toString()));
        content.add(new TextContent(new Pair("Resource group", "rg").toString()));
        content.add(new TextContent(new Pair("Template specs name", "ts").toString()));
        content.add(new TextContent(new Pair("Azure Migrate project", "migr").toString()));
        content.add(new TextContent(new Pair("Database Migration Service instance", "dms").toString()));
        content.add(new TextContent(new Pair("Recovery Services vault", "rsv").toString()));
        content.add(new TextContent(new Pair("Application gateway", "agw").toString()));
        content.add(new TextContent(new Pair("Application security group (ASG)", "asg").toString()));
        content.add(new TextContent(new Pair("CDN profile", "cdnp").toString()));
        content.add(new TextContent(new Pair("CDN endpoint", "cdne").toString()));
        content.add(new TextContent(new Pair("Connections", "con").toString()));
        content.add(new TextContent(new Pair("DNS", "< DNS domain name >").toString()));
        content.add(new TextContent(new Pair("DNS forwarding ruleset", "dnsfrs").toString()));
        content.add(new TextContent(new Pair("DNS private resolver", "dnspr").toString()));
        content.add(new TextContent(new Pair("DNS private resolver inbound endpoint", "in").toString()));
        content.add(new TextContent(new Pair("DNS private resolver outbound endpoint", "out").toString()));
        content.add(new TextContent(new Pair("DNS zone", "< DNS domain name >").toString()));
        content.add(new TextContent(new Pair("Firewall", "afw").toString()));
        content.add(new TextContent(new Pair("Firewall policy", "afwp").toString()));
        content.add(new TextContent(new Pair("ExpressRoute circuit", "erc").toString()));
        content.add(new TextContent(new Pair("ExpressRoute direct", "erd").toString()));
        content.add(new TextContent(new Pair("ExpressRoute gateway", "ergw").toString()));
        content.add(new TextContent(new Pair("Front Door (Standard/Premium) profile", "afd").toString()));
        content.add(new TextContent(new Pair("Front Door (Standard/Premium) endpoint", "fde").toString()));
        content.add(new TextContent(new Pair("Front Door firewall policy", "fdfp").toString()));
        content.add(new TextContent(new Pair("Front Door (classic)", "afd").toString()));
        content.add(new TextContent(new Pair("IP group", "ipg").toString()));
        content.add(new TextContent(new Pair("Load balancer (internal)", "lbi").toString()));
        content.add(new TextContent(new Pair("Load balancer (external)", "lbe").toString()));
        content.add(new TextContent(new Pair("Load balancer rule", "rule").toString()));
        content.add(new TextContent(new Pair("Local network gateway", "lgw").toString()));
        content.add(new TextContent(new Pair("NAT gateway", "ng").toString()));
        content.add(new TextContent(new Pair("Network interface (NIC)", "nic").toString()));
        content.add(new TextContent(new Pair("Network security group (NSG)", "nsg").toString()));
        content.add(new TextContent(new Pair("Network security group (NSG) security rules", "nsgsr").toString()));
        content.add(new TextContent(new Pair("Network Watcher", "nw").toString()));
        content.add(new TextContent(new Pair("Private Link", "pl").toString()));
        content.add(new TextContent(new Pair("Private endpoint", "pep").toString()));
        content.add(new TextContent(new Pair("Public IP address", "pip").toString()));
        content.add(new TextContent(new Pair("Public IP address prefix", "ippre").toString()));
        content.add(new TextContent(new Pair("Route filter", "rf").toString()));
        content.add(new TextContent(new Pair("Route server", "rtserv").toString()));
        content.add(new TextContent(new Pair("Route table", "rt").toString()));
        content.add(new TextContent(new Pair("Service endpoint policy", "se").toString()));
        content.add(new TextContent(new Pair("Traffic Manager profile", "traf").toString()));
        content.add(new TextContent(new Pair("User defined route (UDR)", "udr").toString()));
        content.add(new TextContent(new Pair("Virtual network", "vnet").toString()));
        content.add(new TextContent(new Pair("Virtual network gateway", "vgw").toString()));
        content.add(new TextContent(new Pair("Virtual network manager", "vnm").toString()));
        content.add(new TextContent(new Pair("Virtual network peering", "peer").toString()));
        content.add(new TextContent(new Pair("Virtual network subnet", "snet").toString()));
        content.add(new TextContent(new Pair("Virtual WAN", "vwan").toString()));
        content.add(new TextContent(new Pair("Virtual WAN Hub", "vhub").toString()));
        content.add(new TextContent(new Pair("Azure Bastion", "bas").toString()));
        content.add(new TextContent(new Pair("Key vault", "kv").toString()));
        content.add(new TextContent(new Pair("Key Vault Managed HSM", "kvmhsm").toString()));
        content.add(new TextContent(new Pair("Managed identity", "id").toString()));
        content.add(new TextContent(new Pair("SSH key", "sshkey").toString()));
        content.add(new TextContent(new Pair("VPN Gateway", "vpng").toString()));
        content.add(new TextContent(new Pair("VPN connection", "vcn").toString()));
        content.add(new TextContent(new Pair("VPN site", "vst").toString()));
        content.add(new TextContent(new Pair("Web Application Firewall (WAF) policy", "waf").toString()));
        content.add(new TextContent(new Pair("Web Application Firewall (WAF) policy rule group", "wafrg").toString()));
        content.add(new TextContent(new Pair("Azure StorSimple", "ssimp").toString()));
        content.add(new TextContent(new Pair("Backup Vault name", "bvault").toString()));
        content.add(new TextContent(new Pair("Backup Vault policy", "bkpol").toString()));
        content.add(new TextContent(new Pair("File share", "share").toString()));
        content.add(new TextContent(new Pair("Storage account", "st").toString()));
        content.add(new TextContent(new Pair("Storage Sync Service name", "sss").toString()));
        content.add(new TextContent(new Pair("Virtual desktop host pool", "vdpool").toString()));
        content.add(new TextContent(new Pair("Virtual desktop application group", "vdag").toString()));
        content.add(new TextContent(new Pair("Virtual desktop workspace", "vdws").toString()));
        content.add(new TextContent(new Pair("Virtual desktop scaling plan", "vdscaling").toString()));

        mcpLog.info("Returning " + content.size() + " abbreviations");
        return ToolResponse.success(content);
    }
}

record Pair(String key, String value) {
}
