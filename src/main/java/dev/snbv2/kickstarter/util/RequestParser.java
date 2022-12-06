package dev.snbv2.kickstarter.util;

import java.util.ArrayList;
import java.util.List;

import dev.snbv2.kickstarter.controllers.DeploymentRequest;
import dev.snbv2.kickstarter.controllers.GatewayRequest;
import dev.snbv2.kickstarter.controllers.ServiceRequest;
import dev.snbv2.kickstarter.controllers.VirtualServiceRequest;
import dev.snbv2.kickstarter.types.Container;
import dev.snbv2.kickstarter.types.Deployment;
import dev.snbv2.kickstarter.types.Gateway;
import dev.snbv2.kickstarter.types.Kind;
import dev.snbv2.kickstarter.types.Metadata;
import dev.snbv2.kickstarter.types.PodSpec;
import dev.snbv2.kickstarter.types.Protocol;
import dev.snbv2.kickstarter.types.Service;
import dev.snbv2.kickstarter.types.TLSMode;
import dev.snbv2.kickstarter.types.VirtualService;
import dev.snbv2.kickstarter.types.Deployment.DeploymentSpec;
import dev.snbv2.kickstarter.types.Deployment.DeploymentStrategy;
import dev.snbv2.kickstarter.types.Gateway.GatewaySpec;
import dev.snbv2.kickstarter.types.Gateway.Port;
import dev.snbv2.kickstarter.types.Gateway.Server;
import dev.snbv2.kickstarter.types.Gateway.ServerTLSSettings;

public class RequestParser {
    
    public static Deployment parseDeploymentRequest(DeploymentRequest deploymentRequest) {
        
        Kind kind = Kind.DEPLOYMENT;

        Metadata metadata = new Metadata();
        metadata.setName(deploymentRequest.getName());
        metadata.setNamespace(deploymentRequest.getNamespace());

        Container container = new Container();
        container.setName(deploymentRequest.getContainerName());
        container.setImage(deploymentRequest.getContainerImage());
        container.setImagePullPolicy(
            Container.ImagePullPolicy.findByValue(deploymentRequest.getContainerImagePullPolicy()));
        container.setPort(deploymentRequest.getContainerPort());
        List<Container> containers = new ArrayList<Container>();
        containers.add(container);

        PodSpec pod = new PodSpec();
        pod.setContainers(containers);
        pod.setServiceAccountName(deploymentRequest.getServiceAccountName());
        
        Deployment deployment = new Deployment();
        deployment.setKind(kind);
        deployment.setMetadata(metadata);

        DeploymentSpec deploymentSpec = deployment.new DeploymentSpec();
        deploymentSpec.setDeploymentStrategy(
            DeploymentStrategy.findByValue(deploymentRequest.getDeploymentStrategy()));
        deploymentSpec.setReplicas(deploymentRequest.getReplicas());
        deploymentSpec.setPod(pod);
        deployment.setSpec(deploymentSpec);

        return deployment;
    }

    public static Service parseServiceRequest(ServiceRequest serviceRequest) {
        
        Kind kind = Kind.SERVICE;

        Metadata metadata = new Metadata();
        metadata.setName(serviceRequest.getName());
        metadata.setNamespace(serviceRequest.getNamespace());

        Service service = new Service();
        service.setKind(kind);
        service.setMetadata(metadata);

        Service.ServiceSpec serviceSpec = service.new ServiceSpec();
        serviceSpec.setPort(serviceRequest.getServicePort());
        service.setSpec(serviceSpec);

        return service;
    }

    public static Gateway parseGatewayRequest(GatewayRequest gatewayRequest) {

        Kind kind = Kind.GATEWAY;

        Metadata metadata = new Metadata();
        metadata.setName(gatewayRequest.getName());
        metadata.setNamespace(gatewayRequest.getNamespace());

        Gateway gateway = new Gateway();
        gateway.setKind(kind);
        gateway.setMetadata(metadata);

        Port port = gateway.new Port();
        port.setName(gatewayRequest.getPortName());
        port.setPort(gatewayRequest.getPort());
        port.setProtocol(Protocol.findByValue(gatewayRequest.getProtocol()));
        port.setTargetPort(gatewayRequest.getTargetPort());

        ServerTLSSettings tlsSettings = gateway.new ServerTLSSettings();
        tlsSettings.setHttpsRedirect(gatewayRequest.getHttpsRedirect());
        tlsSettings.setCredentialName(gatewayRequest.getCredentialName());
        tlsSettings.setMode(TLSMode.findByValue(gatewayRequest.getTlsMode()));

        Server server = gateway.new Server();
        List<String> hosts = new ArrayList<String>();
        hosts.add(gatewayRequest.getHost());
        server.setHosts(hosts);
        server.setName(gatewayRequest.getServerName());
        server.setPort(port);
        server.setTls(tlsSettings);

        GatewaySpec gatewaySpec = gateway.new GatewaySpec();
        gatewaySpec.setServer(server);

        gateway.setSpec(gatewaySpec);

        return gateway;
    }

    public static VirtualService parseVirtualServiceRequest(VirtualServiceRequest virtualServiceRequest) {
        
        Kind kind = Kind.VIRTUAL_SERVICE;

        Metadata metadata = new Metadata();
        metadata.setName(virtualServiceRequest.getName());
        metadata.setNamespace(virtualServiceRequest.getNamespace());

        VirtualService virtualService = new VirtualService();
        virtualService.setKind(kind);
        virtualService.setMetadata(metadata);

        VirtualService.Destination destination = virtualService.new Destination();
        destination.setHost(virtualServiceRequest.getDestinationHost());
        destination.setPortNumber(virtualServiceRequest.getDestinationPortNumber());

        VirtualService.VirtualServiceSpec virtualServiceSpec = virtualService.new VirtualServiceSpec();
        virtualServiceSpec.setDestination(destination);
        virtualServiceSpec.setGatewayName(virtualServiceRequest.getGatewayName());
        virtualServiceSpec.setHost(virtualServiceRequest.getHost());
        
        virtualService.setSpec(virtualServiceSpec);

        return virtualService;
    }
}
