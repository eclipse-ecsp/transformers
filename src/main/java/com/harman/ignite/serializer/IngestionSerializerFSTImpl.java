/*
 *
 *
 *   ******************************************************************************
 *
 *    Copyright (c) 2023-24 Harman International
 *
 *
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *
 *    you may not use this file except in compliance with the License.
 *
 *    You may obtain a copy of the License at
 *
 *
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 *    Unless required by applicable law or agreed to in writing, software
 *
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *    See the License for the specific language governing permissions and
 *
 *    limitations under the License.
 *
 *
 *
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    *******************************************************************************
 *
 *
 */

package com.harman.ignite.serializer;

import com.harman.ignite.entities.IgniteBlobEvent;
import com.harman.ignite.entities.IgniteDeviceAwareBlobEvent;
import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTUtil;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.Properties;

/**
 * This Class provides method implementation of IngestionSerializer.
 *
 */

public class IngestionSerializerFSTImpl implements IngestionSerializer {
    
    public static final byte[] STREAM_MAGIC_IN_BYTES;
    public static final String STREAM_MAGIC_STR = "aced"; // 0xaced get from
    // ObjectStreamConstants.STREAM_MAGIC
    public static final int STREAM_MAGIC_BYTES_LEN = 2;
    private static final String DEVICE_AWARE_ENABLED = "device.aware.enabled";
    public static final int TWO = 2;
    public static final int SIXTEEN = 16;
    public static final int FOUR = 4;
    public static final int HEX_BASE_16 = 0xff;
    public static final int EIGHT = 8;
    private static IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IngestionSerializerFSTImpl.class);
    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    private static Properties properties;

    static {
        // Do not use shortpath for the common string while doing serialization.
        conf.setShareReferences(false);
        // convert the hex aced to byte[]
        STREAM_MAGIC_IN_BYTES = hexStringToByteArray(STREAM_MAGIC_STR);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / TWO];
        for (int i = 0; i < len; i += TWO) {
            data[i / TWO] = (byte) ((Character.digit(s.charAt(i), SIXTEEN) << FOUR)
                    + Character.digit(s.charAt(i + 1), SIXTEEN));
        }

        return data;
    }

    private static void loadProperties() throws IOException {

        if (ObjectUtils.isEmpty(properties)) {
            properties = new Properties();
            LOGGER.info("Loading properties from environment");
            System.getenv().forEach((k, v) -> properties.setProperty(k.replaceAll("_", "."), v));
            LOGGER.info("device.aware.enabled:" + properties.get(DEVICE_AWARE_ENABLED));
        }
    }

    @Override
    public byte[] serialize(IgniteBlobEvent obj) {
        FSTObjectOutput objectOutput = conf.getObjectOutput();
        try {
            // Add the magic bytes in output stream.
            objectOutput.write(STREAM_MAGIC_IN_BYTES);
            objectOutput.writeObject(obj);
            return objectOutput.getCopyOfWrittenBuffer();
        } catch (IOException e) {
            FSTUtil.<RuntimeException>rethrow(e);
        }
        return new byte[0];
    }

    @Override
    public IgniteBlobEvent deserialize(byte[] b) {
        try {
            // discard the first two magic bytes from input stream.
            loadProperties();
            boolean deviceAwareEnableFlag = Boolean.parseBoolean((String) properties.get(DEVICE_AWARE_ENABLED));
            Object object = (Object) conf.getObjectInputCopyFrom(b, STREAM_MAGIC_BYTES_LEN, 
                    b.length - STREAM_MAGIC_BYTES_LEN).readObject();
            return deviceAwareEnableFlag ? (IgniteDeviceAwareBlobEvent) object : (IgniteBlobEvent) object;

        } catch (Exception e) {
            FSTUtil.<RuntimeException>rethrow(e);
        }
        return null;
    }

    @Override
    public boolean isSerialized(byte[] bytes) {
        // Convert to short from reading the first two bytes from byte array
        short sm = (short) (((bytes[0] & HEX_BASE_16) << EIGHT) + (bytes[1] & HEX_BASE_16));
        return sm == ObjectStreamConstants.STREAM_MAGIC;
    }
}
