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
 * Test class.
 */

@RunWith(Parameterized.class)
public class JacksonMapperConfigInvalidParamsSubTypeTest {

    String customSubType;

    public JacksonMapperConfigInvalidParamsSubTypeTest(String customSubType) {
        this.customSubType = customSubType;
    }

    /**
     * Helper method to pass each item in the array as an argument to the test method.
     */
    @Parameterized.Parameters
    public static Collection<?> customSubTypeList() {
        return Arrays.asList(new Object[] { "com.harman.ignite.transform.config.ItemToUserNameMapping:",
                                            ":newType",
                                            "newType" });
    }

    /**
     * Testing insufficient params in sub type.
     */
    @Test(expected = IllegalStateException.class)
    public void testInsufficientParamsSubtype() {

        JacksonMapperConfig config = new JacksonMapperConfig();
        config.setCustomSubtypes(customSubType);

        // get the object mapper
        config.jsonObjectMapper();
    }
}
