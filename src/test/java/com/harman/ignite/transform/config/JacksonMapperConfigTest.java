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

package com.harman.ignite.transform.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Testing Jackson Mapper config class.
 */
public class JacksonMapperConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonMapperConfigTest.class);

    /**
     * Test to add custom serializer to the mapper.
     *
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Test
    public void testCustomSerializer() throws JsonProcessingException {
        JacksonMapperConfig config = new JacksonMapperConfig();
        // adding the custom serializer for Sample class
        String customSerializerList =
                "com.harman.ignite.transform.config."
                        + "ItemToUserNameMapping:com.harman.ignite.transform.config.ItemToUserNameMappingSerializer";
        config.setCustomSerializers(customSerializerList);

        // get the object mapper
        ObjectMapper mapper = config.jsonObjectMapper();

        ItemToUserNameMapping s = new ItemToUserNameMapping(1, "soap", "user1");
        String actualString = mapper.writeValueAsString(s);
        // as per the serializer, "-new will be added to the user1 string and
        // the field name will be owner
        LOGGER.info("Modified sample string:{}", actualString);

        String expectedString = "{\"id\":1,\"itemName\":\"soap\",\"owner\":\"user1-new\"}";
        Assert.assertEquals(expectedString, actualString);

    }

    /**
     * Test to add custom deserializer.
     *
     * @throws IOException         I/O Exception
     * @throws JsonMappingException JsonMappingException
     * @throws JsonParseException  JsonParseException
     */
    @Test
    public void testCustomDeserializer() throws JsonParseException, JsonMappingException, IOException {
        JacksonMapperConfig config = new JacksonMapperConfig();
        String customDeserializerList = "com.harman.ignite.transform.config"
                + ".ItemToUserNameMapping:com.harman.ignite.transform.config.ItemToUserNameDeserializer";
        config.setCustomDeserializers(customDeserializerList);

        // get the object mapper
        ObjectMapper mapper = config.jsonObjectMapper();
        String itemString = "{\"id\":1,\"itemName\":\"soap\",\"userName\":\"user1-new\"}";
        ItemToUserNameMapping obj = mapper.readValue(itemString, ItemToUserNameMapping.class);

        // in the deserializer class, we have changed the value of itemnNam to
        // pen and userName to "user2"
        Assert.assertNotNull(obj);

        Assert.assertEquals("Item names must be same", "pen", obj.getItemName());
        Assert.assertEquals("User names must be same", "user2", obj.getUserName());
    }
    
    /**
     * Negative test cases in which we have added deserializer class in case
     * serialization is expected.
     */
    @Test(expected = IllegalStateException.class)
    public void testImproperClassProvidedInSerializer() {

        JacksonMapperConfig config = new JacksonMapperConfig();
        String customSerializerList = "com.harman.ignite.transform.config.ItemToUserNameMapping:"
                + "com.harman.ignite.transform.config.ItemToUserNameDeserializer";
        config.setCustomSerializers(customSerializerList);

        // get the object mapper
        config.jsonObjectMapper();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsufficientParamsSubtype() {
        Properties p = new Properties();
        JacksonMapperConfig config = new JacksonMapperConfig(p);
        String customSubtypes = "newType";
        config.setCustomSubtypes(customSubtypes);

        // get the object mapper
        config.jsonObjectMapper();
    }

}
