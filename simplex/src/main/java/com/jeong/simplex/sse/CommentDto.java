package com.jeong.simplex.sse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class CommentDto {

    private String comment;

}
