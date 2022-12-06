package dev.snbv2.kickstarter.controllers;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import dev.snbv2.kickstarter.types.Deployment;
import dev.snbv2.kickstarter.types.Gateway;
import dev.snbv2.kickstarter.types.Service;
import dev.snbv2.kickstarter.types.VirtualService;
import dev.snbv2.kickstarter.util.RequestParser;

@RestController
public class ApplicationController {
    
    @Autowired
    TemplateEngine templateEngine;

    @PostMapping(value = "/application", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody byte[] getApplication(@RequestBody ApplicationRequest applicationRequest) {

        Context context = new Context(Locale.getDefault());

        DeploymentRequest deploymentRequest = new DeploymentRequest();
        deploymentRequest.setContainerName(applicationRequest.getContainerName());
        deploymentRequest.setContainerImage(applicationRequest.getContainerImage());
        deploymentRequest.setContainerImagePullPolicy(applicationRequest.getContainerImagePullPolicy());
        deploymentRequest.setContainerPort(applicationRequest.getContainerPort());
        deploymentRequest.setDeploymentStrategy(applicationRequest.getDeploymentStrategy());
        deploymentRequest.setName(applicationRequest.getName());
        deploymentRequest.setNamespace(applicationRequest.getNamespace());
        deploymentRequest.setReplicas(applicationRequest.getReplicas());
        deploymentRequest.setServiceAccountName(applicationRequest.getName());
        Deployment deployment = RequestParser.parseDeploymentRequest(deploymentRequest);
        context.setVariable("deployment", deployment);

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setName(applicationRequest.getName());
        serviceRequest.setNamespace(applicationRequest.getNamespace());
        serviceRequest.setServicePort(applicationRequest.getServicePort());
        Service service = RequestParser.parseServiceRequest(serviceRequest);
        context.setVariable("service", service);

        if (Boolean.TRUE.equals(applicationRequest.getIncludeGateway())) {
            GatewayRequest gatewayRequest = new GatewayRequest();
            gatewayRequest.setCredentialName(applicationRequest.getCredentialName());
            gatewayRequest.setHost(applicationRequest.getHost());
            gatewayRequest.setHttpsRedirect(applicationRequest.getHttpsRedirect());
            gatewayRequest.setName(applicationRequest.getName() + "-gw");
            gatewayRequest.setNamespace(applicationRequest.getNamespace());
            gatewayRequest.setPort(applicationRequest.getGatewayPort());
            gatewayRequest.setPortName(applicationRequest.getGatewayPortName());
            gatewayRequest.setProtocol(applicationRequest.getProtocol());
            gatewayRequest.setTlsMode(applicationRequest.getTlsMode());
            Gateway gateway = RequestParser.parseGatewayRequest(gatewayRequest);
            context.setVariable("gateway", gateway);
        }

        if (Boolean.TRUE.equals(applicationRequest.getIncludeVirtualService())) {
            VirtualServiceRequest virtualServiceRequest = new VirtualServiceRequest();
            virtualServiceRequest.setName(applicationRequest.getName() + "-vs");
            virtualServiceRequest.setNamespace(applicationRequest.getNamespace());
            virtualServiceRequest.setHost(applicationRequest.getHost());
            virtualServiceRequest.setGatewayName(applicationRequest.getName() + "-gw");
            virtualServiceRequest.setDestinationHost(applicationRequest.getName());
            virtualServiceRequest.setDestinationPortNumber(applicationRequest.getServicePort());
            VirtualService virtualService = RequestParser.parseVirtualServiceRequest(virtualServiceRequest);
            context.setVariable("virtualService", virtualService);
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        templateEngine.process("application", context, writer);
        return out.toByteArray();
    }
}
