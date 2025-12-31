package com.forum.forum.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicTo {

    private Integer id;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 4, message = "{validation.title.size}")
    private String title;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 10, message = "{validation.content.size}")
    private String content;
}
