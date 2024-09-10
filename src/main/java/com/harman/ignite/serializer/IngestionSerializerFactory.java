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

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * This Class provides factory methods for IngestionSerializer.
 */
public class IngestionSerializerFactory {
  
    private static IngestionSerializer instance;
    private static final Object LOCK = new Object();
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IngestionSerializerFactory.class);

    private IngestionSerializerFactory() {

    }
    /**
     * Returns the instance of the class for the given fully qualified class name.
     */
    
    public static IngestionSerializer getInstance(String serializerClassName) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    load(serializerClassName);
                }
            }
        }
        return instance;
    }

    private static void load(String className) {

        if (StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("Serializer class name cannot be null or empty.");
        }
        try {
            LOGGER.debug("Load IngestionSerializerFactory with classname {}", className);
            instance = (IngestionSerializer) IngestionSerializerFactory.class.getClassLoader().loadClass(className)
                    .getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException
                | NoSuchMethodException e) {
            LOGGER.error(className + "  is not available on the classpath");
            throw new IllegalArgumentException(className + "  is not available on the classpath");
        }
    }
}