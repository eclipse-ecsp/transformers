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

package com.harman.ignite.transform;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.harman.ignite.domain.EventAttribute;
import com.harman.ignite.domain.IgniteEventSource;
import com.harman.ignite.entities.IgniteEvent;
import com.harman.ignite.entities.IgniteEventBase;
import com.harman.ignite.transform.config.JacksonMapperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * This Class provides method for transformation of Device Message to Ignite Event.
 *
 * @author avadakkootko
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceMessageIgniteEventTransformer implements Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessageIgniteEventTransformer.class);
    @Autowired
    private ObjectMapper jsonMapper;
    private ObjectWriter deviceWriter;

    /**
     * Method to transform Device Message to IgniteEvent.
     *
     */
    public DeviceMessageIgniteEventTransformer() {
        // In case if this object is created using c'tor not via spring, then we
        // have to initialize json mapper and device writer
        if (null == jsonMapper) {
            jsonMapper = new JacksonMapperConfig().jsonObjectMapper();
            initialize();
        }
    }

    @Override
    public IgniteEvent fromBlob(byte[] value, Optional<IgniteEventBase> header) {
        throw new DeviceMessageIgniteEventTransformException("Implementation for fromBlob method is not "
                + "available in DeviceMessageIgniteEventTransformer.");
    }

    @Override
    public byte[] toBlob(IgniteEvent value) {

        if (null == value) {
            LOGGER.error("Received null ignite event value, cannot conver to blob.");
            throw new TransformerSerDeException("Received null ignite event value");
        }

        LOGGER.debug("Converting device facing ignite event:{} to byte array", value);

        byte[] blobData = null;
        try {
            blobData = deviceWriter.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to convert the ignite event to bytes.", e);
            throw new TransformerSerDeException("Unable to conver the ignite event:" + value.toString()
                    + " to byte array");
        }
        return blobData;
    }

    @Override
    public String getSource() {
        return IgniteEventSource.IGNITE;
    }

    /**
     * Initializer Method .
     *
     */
    @PostConstruct
    public void initialize() {
        FilterProvider filter = new SimpleFilterProvider().addFilter(EventAttribute.EVENT_FILTER, getPropertyFilter())
                .setDefaultFilter(SimpleBeanPropertyFilter.serializeAll());
        deviceWriter = jsonMapper.writer(filter);
    }

    private PropertyFilter getPropertyFilter() {
        return new SimpleBeanPropertyFilter() {
            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider,
                    PropertyWriter writer)
                    throws Exception {
                if (include(writer)) {
                    String attributeName = writer.getName();
                    if (!(EventAttribute.REQUEST_ID.equals(attributeName)
                            || EventAttribute.VEHICLE_ID.equals(attributeName)
                            || EventAttribute.DEVICE_DELIVERY_CUTOFF.equals(attributeName))) {
                        writer.serializeAsField(pojo, jgen, provider);
                    }
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, provider);
                }
            }

            @Override
            protected boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            protected boolean include(PropertyWriter writer) {
                return true;
            }
        };
    }

    /**
     * Method exposed for testing.
     *
     * @param writer : ObjectWriter
     */
    void setObjectWriter(ObjectWriter writer) {
        this.deviceWriter = writer;
    }

}
