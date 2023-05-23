package com.redhat.mta.rest;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.kie.internal.task.api.AuditTask;

import com.redhat.mta.types.Application;
import com.redhat.mta.types.TaskGroup;

import io.quarkus.arc.Priority;
import io.quarkus.restclient.NoopHostnameVerifier;

// This is a programatic implementation because there is no
// way to inject the rest client with a custom ssl context (for non-verifying needs for example...)
// This is not recommended but helps developing with services with self-signed certs we that can't be verified.
@Priority(value = 1)
@RestClient
@ApplicationScoped
public class MTARemoteServiceImpl implements MTARemoteService {


    private MTARemoteService client;
    public MTARemoteServiceImpl() {
        
        try {
            
            var sslContext = SSLContext.getInstance("TLS");
            var tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[] { tm }, null);
            client = RestClientBuilder.newBuilder().baseUri(URI.create("https://mta-openshift-mta.apps.parodos-dev.projects.ecosystem.sysdeseng.com"))
                .hostnameVerifier(new NoopHostnameVerifier())
                .sslContext(sslContext)
                .build(MTARemoteService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    @Override
    public List<Application> getAll() {
        return client.getAll();
    }

    @Override
    public Application create(Application application) {
        return client.create(application);
    }

    @Override
    public TaskGroup get(int id) {
        return client.get(id);
    }

    @Override
    public TaskGroup create(TaskGroup taskGroup) {
        return client.create(taskGroup);
    }

    @Override
    public void submit(int id, TaskGroup tg) {
        client.submit(tg.id(), tg);
    }
}

