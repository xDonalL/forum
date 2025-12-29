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

    @NotBlank(message = "should not be empty")
    @Size(min = 4, message = "min 4 character")
    private String title;

    @NotBlank(message = "should not be empty")
    @Size(min = 10, message = "min 10 character")
    private String content;
}
