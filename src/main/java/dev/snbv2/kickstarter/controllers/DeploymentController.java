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
import dev.snbv2.kickstarter.util.RequestParser;

@RestController
public class DeploymentController {
    
    @Autowired
    TemplateEngine templateEngine;
    
    @PostMapping(value = "/deployment", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody byte[] getDeployment(@RequestBody DeploymentRequest deploymentRequest) {

        Deployment deployment = RequestParser.parseDeploymentRequest(deploymentRequest);

        Context context = new Context(Locale.getDefault());
        context.setVariable("deployment", deployment);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        templateEngine.process("deployment", context, writer);
        return out.toByteArray();
    }
}
