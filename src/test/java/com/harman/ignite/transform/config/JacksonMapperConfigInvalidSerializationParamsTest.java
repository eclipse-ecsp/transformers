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


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test case for mapping class to serializer.
 */

@RunWith(Parameterized.class)
public class JacksonMapperConfigInvalidSerializationParamsTest {
    String customSerializerList;

    public JacksonMapperConfigInvalidSerializationParamsTest(String customSerializerList) {
        this.customSerializerList = customSerializerList;
    }
    
    /**
     * Helper method to pass each item in the array as an argument to the test method.
     */
    @Parameterized.Parameters
    public static Collection<?> customSerializerList() {
        return Arrays.asList(new Object[] { "com.harman.ignite.transform.config.ItemToUserNameMapping:",
                                            ":com.harman.ignite.transform.config.ItemToUserNameMappingSerializer" });
    }

    /**
     * Negative scenario where proper pair is not given.
     */
    
    @Test(expected = IllegalStateException.class)
    public void testInsufficientSerializationParams() {

        JacksonMapperConfig config = new JacksonMapperConfig();
        config.setCustomSerializers(customSerializerList);

        // get the object mapper
        config.jsonObjectMapper();
    }
}
