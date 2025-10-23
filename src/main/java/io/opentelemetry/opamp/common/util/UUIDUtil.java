package io.opentelemetry.opamp.common.util;

import com.fasterxml.uuid.Generators;

import java.nio.ByteBuffer;
import java.util.UUID;

public enum UUIDUtil {
    INSTANCE;
    public byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public UUID generateUUIDv7() {
        return Generators.timeBasedEpochGenerator().generate();
    }
}
