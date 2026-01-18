package com.forum.forum.service.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SafeMarkdownService {

    private final MarkdownService markdownService;
    private final HtmlSanitizerService htmlSanitizerService;

    public String toSafeHtml(String markdown) {
        String html = markdownService.toHtml(markdown);
        return htmlSanitizerService.sanitize(html);
    }
}