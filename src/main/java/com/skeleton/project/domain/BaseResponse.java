package com.skeleton.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @since 1.0
 * @author raysmets
 * Dummy base response DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

}
