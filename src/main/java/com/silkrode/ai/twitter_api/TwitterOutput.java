package com.silkrode.ai.twitter_api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TwitterOutput {
    List<Map<String, Object>> _result;
    boolean ok;
    String result;
    String id;
}
