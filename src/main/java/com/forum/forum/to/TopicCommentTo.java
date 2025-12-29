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
public class TopicCommentTo {

    private Integer id;

    private Integer topicId;

    @NotBlank(message = "should not be empty")
    @Size(min = 4, message = "min 4 character")
    private String comment;
}
