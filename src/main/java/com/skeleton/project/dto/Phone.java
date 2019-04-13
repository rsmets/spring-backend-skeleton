package com.skeleton.project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
public class Phone {

    @Id
    String _id;

    String phone;
    String verificationCode;
    Boolean primary;
    Boolean verified;
    User user;

    Date _create_at;
    Date _updated_at;

    // ******************************************************************************
    // Necessary to explicitly have these different json keys map to same attribute
    // due to incoming either a) directly from db ('_id') or as a Pointer in other
    // object ('objectId')
    // ******************************************************************************

    @JsonSetter("objectId")
    public void setObjectId(String id) {
        this._id = id;
    }

    @JsonSetter("_id")
    public void setId(String id) {
        this._id = id;
    }

    @JsonGetter("_id")
    public String getId(String id) {
        return this._id;
    }

}
