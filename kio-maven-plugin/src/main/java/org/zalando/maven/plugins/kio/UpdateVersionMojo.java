package org.zalando.maven.plugins.kio;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.zalando.stups.clients.kio.CreateOrUpdateVersionRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Mojo(name = "updateVersion", requiresProject = true, threadSafe = false, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class UpdateVersionMojo extends AbstractMojo {

    private static final String TOKENS_YAML = "tokens.yaml";

    private static final String KIO_TOKEN_KEY = "kio";

    private static final String ACCESS_TOKEN = "access_token";

    private static final String BEARER = "Bearer ";

    private static final String AUTHORIZATION = "Authorization";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Parameter(defaultValue = "${project.version}", required = true)
    protected String versionId;

    @Parameter(required = true)
    protected String applicationId;

    @Parameter(required = true)
    protected String artifact;

    @Parameter(defaultValue = "kio-maven-plugin")
    protected String notes = "kio-maven-plugin";

    @Parameter(required = true)
    protected String kioBaseUrl;

    @Parameter(required = false, defaultValue = "false")
    protected boolean skip = false;

    @Parameter(required = false, defaultValue = "false")
    private boolean enableLog = false;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("PLUGIN WAS SET TO SKIP, CHECK POM.XML");
            return;
        }

        CreateOrUpdateVersionRequest r = buildRequestObject();

        executeRequest(r);
    }

    protected CreateOrUpdateVersionRequest buildRequestObject() {
        CreateOrUpdateVersionRequest r = new CreateOrUpdateVersionRequest();
        r.setArtifact(artifact);
        r.setNotes(notes);
        return r;
    }

    protected void executeRequest(CreateOrUpdateVersionRequest r) throws MojoExecutionException {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        try {
            final String token = getToken();
            final String url = buildUrl();
            if(enableLog){
                getLog().info("URL_USED : " + url);
                getLog().info("TOKEN_USED : " + token.substring(0, 8));
            }
            RequestBody body = RequestBody.create(JSON, buildBodyContent(r));
            Request request = new Request.Builder().url(url).put(body)
                    .addHeader(AUTHORIZATION, BEARER + token).build();
            try (Response response = client.newCall(request).execute()) {
                String result = response.body().string();
                getLog().info(result);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    protected String buildBodyContent(CreateOrUpdateVersionRequest r) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(r);
    }

    protected String buildUrl() {
        return kioBaseUrl + "/apps/" + applicationId + "/versions/" + versionId;
    }

    protected String getToken() throws JsonProcessingException, IOException {
        String os = System.getProperty("os.name");
        String applicationSupportDirectory = null;
        if (os.startsWith("Mac OS")) {
            applicationSupportDirectory = System.getProperty("user.home") + "/Library/Application Support/zign";
        } else {
            applicationSupportDirectory = System.getProperty("user.home") + "/.config/zign";
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        @SuppressWarnings("unchecked")
        Map<String, LinkedHashMap<String, String>> map = mapper
                .readValue(new File(applicationSupportDirectory, TOKENS_YAML), Map.class);
        LinkedHashMap<String, String> json = map.get(KIO_TOKEN_KEY);
        if (json == null) {
            getLog().error("no token found for 'kio', run 'zign token kio' before.");
            throw new IOException("No Token found for 'kio'");
        }
        return json.get(ACCESS_TOKEN);
    }

}
