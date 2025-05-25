package dev.kobura.evascript.runtime.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@AllArgsConstructor
@ToString
public class ContextIdentity {

    private String name;
    private boolean constant;
    private Instant creation;
    private long expiration;

}
