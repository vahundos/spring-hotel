package com.vahundos.spring.hotel;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static String getFixtureContent(String relativePath, Object... params) {
        URL resource = Resources.getResource("fixtures/" + relativePath);
        try {
            return String.format(Resources.toString(resource, Charsets.UTF_8), params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
