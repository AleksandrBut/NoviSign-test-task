package com.test.task.novisign.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ExceptionResponse(String reason, List<String> messages) {

}
