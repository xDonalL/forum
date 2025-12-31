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

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 4, message = "{validation.comment.size}")
    private String comment;
}
