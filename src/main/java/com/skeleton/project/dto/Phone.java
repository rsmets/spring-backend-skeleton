package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.ObjectId;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Phone {

    @ObjectId
    String id;

    String phone;
    String verificationCode;
    Boolean primary;
    Boolean verified;
    User user;

    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    @JsonSetter("objectId")
    private void setObjectId(String id) {
        this.id = id;
    }

    @JsonSetter("_id")
    private void setId(String id) {
        this.id = id;
    }

}
