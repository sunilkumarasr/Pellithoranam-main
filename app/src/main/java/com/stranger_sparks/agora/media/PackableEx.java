package com.stranger_sparks.agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
