package com.skeleton.project.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @since 1.0
 * @author raysmets
 * Class holding relevant query result information. 
 */
@Data
@Builder
public class QueryResponse {

    @ApiModelProperty(value = "example", required = true)
    private Object example;
}
