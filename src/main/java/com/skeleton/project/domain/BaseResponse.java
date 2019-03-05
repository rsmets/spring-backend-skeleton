package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @since 1.0
 * @author raysmets
 * Dummy base response DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class BaseResponse {

    @ApiModelProperty(value = "example", required = true)
    private Object example;
}
