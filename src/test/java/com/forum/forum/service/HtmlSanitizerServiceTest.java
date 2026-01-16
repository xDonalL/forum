package com.forum.forum.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HtmlSanitizerServiceTest {

    private final HtmlSanitizerService sanitizer = new HtmlSanitizerService();

    @Test
    void shouldRemoveScript() {
        String input = "hello<script>alert(1)</script>";
        String result = sanitizer.sanitize(input);

        assertThat(result).doesNotContain("script");
    }

    @Test
    void shouldAllowBoldAndParagraph() {
        String input = "<p>Hello <b>world</b></p>";
        String result = sanitizer.sanitize(input);

        assertThat(result).contains("<p>")
                .contains("<b>");
    }

    @Test
    void shouldRemoveOnClick() {
        String input = "<a href='https://a.com' onclick='alert(1)'>link</a>";
        String result = sanitizer.sanitize(input);

        assertThat(result).doesNotContain("onclick");
    }
}