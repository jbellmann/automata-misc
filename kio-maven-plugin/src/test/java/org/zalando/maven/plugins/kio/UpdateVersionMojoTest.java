package org.zalando.maven.plugins.kio;

import java.io.IOException;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UpdateVersionMojoTest {

    private UpdateVersionMojo mojo;

    @Before
    public void setUp() {
        mojo = new UpdateVersionMojo();
    }

    @Test
    public void testOsName() {
        String os = System.getProperty("os.name");
        System.out.println(os);
    }

    // run 'zign token -n kio' first
    @Ignore
    @Test
    public void readTokens() throws JsonProcessingException, IOException {
        String token = mojo.getToken();
        Assertions.assertThat(token).isNotEmpty();
        UUID uuid = UUID.fromString(token);
        Assertions.assertThat(uuid).isNotNull();
    }

    @Test
    public void buildUrl() {
        mojo.kioBaseUrl = "https://somewhere.com";
        mojo.artifact = "one.registry.com:5001/automata/fancy-image:0.5.4";
        mojo.applicationId = "fancy-image";
        mojo.versionId = "0.5.4";
        String url = mojo.buildUrl();
        Assertions.assertThat(url).isNotEmpty();
        Assertions.assertThat(url).isEqualTo("https://somewhere.com/apps/fancy-image/versions/0.5.4");
    }

    @Test
    public void buildBody() throws JsonProcessingException {
        mojo.artifact = "one.registry.com:5001/automata/fancy-image:0.5.4";
        mojo.applicationId = "fancy-image";
        mojo.versionId = "0.5.4";
        String contentAsJson = mojo.buildBodyContent(mojo.buildRequestObject());
        Assertions.assertThat(contentAsJson).isNotEmpty();
        System.out.println(contentAsJson);
    }
}
