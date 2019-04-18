package com.skeleton.project.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.skeleton.project.domain.Email;
import com.skeleton.project.domain.Phone;
import com.skeleton.project.domain.User;
import com.skeleton.project.dto.entity.Pointer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDeserializer extends StdDeserializer {
    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class vc) {
        super(vc);
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String id = node.get("_id").asText();
        String username = node.get("username").asText();
        String lastName = node.get("lastName").asText();
        String firstName = node.get("firstName").asText();
        String primaryPhone = node.get("primaryPhone").asText();
        String primaryEmail = node.get("primaryEmail").asText();
        int type = (Integer) node.get("type").numberValue();

        JsonNode phoneNode = node.get("phones");
        JsonNode emailsNode = node.get("emails");

        // TODO make generic method for pointer to T
        List<Phone> phones = handlePhonePointerList(phoneNode);
        List<Email> emails = handleEmailPointerList(emailsNode);

        User res = User.builder()
                .id(id)
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .primaryPhone(primaryPhone)
                .primaryEmail(primaryEmail)
                .type(type)
//                .phones(phones)
//                .emails(emails)
                .build();

        log.debug("full populated user obj: " + res.toString());

        return res;
    }

    private List<Email> handleEmailPointerList(JsonNode jsonNode) {
        List<Email> result = new ArrayList<>();
        for(JsonNode jnode : jsonNode) {
            try {
                com.skeleton.project.dto.entity.Email dto = mapper.readValue(jnode.toString(), com.skeleton.project.dto.entity.Email.class);

                // TODO ACTUALLY GRAB THE DB EMAIL WITH ALL FIELDS POPULATED!!

                result.add(Email.convertFromDto(dto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private List<Phone> handlePhonePointerList(JsonNode jsonNode) {
        List<Phone> result = new ArrayList<>();
        for(JsonNode jnode : jsonNode) {
            try {
                com.skeleton.project.dto.entity.Phone dto = mapper.readValue(jnode.toString(), com.skeleton.project.dto.entity.Phone.class);

                // TODO ACTUALLY GRAB THE DB PHONE WITH ALL FIELDS POPULATED!!

                result.add(Phone.convertFromDto(dto));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private List<Pointer> handlePointerList(JsonNode jsonNode) {
        List<Pointer> result = new ArrayList<>();
        for(JsonNode jnode : jsonNode) {
            try {
                Pointer pointer = mapper.readValue(jnode.toString(), Pointer.class);
                result.add(pointer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static <T> T fromJSON(final TypeReference<T> type,
                                  final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
            e.printStackTrace();
        }
        return data;
    }
}
