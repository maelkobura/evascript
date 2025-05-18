package dev.kobura.evascript.runtime.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class ContextIdentity {

    private String name;
    private boolean constant;
    private Instant creation;
    private long expiration;

}
