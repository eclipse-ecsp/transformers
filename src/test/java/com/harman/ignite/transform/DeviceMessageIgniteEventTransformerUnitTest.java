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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harman.ignite.entities.IgniteEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit Test class for DeviceMessageIgniteEventTransformer.
 *
 * @see DeviceMessageIgniteEventTransformer
 *
 */
public class DeviceMessageIgniteEventTransformerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessageIgniteEventTransformerUnitTest.class);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    IgniteEvent event;
    private DeviceMessageIgniteEventTransformer transformer;

    @Before
    public void setup() {
        transformer = new DeviceMessageIgniteEventTransformer();
    }

    @Test(expected = RuntimeException.class)
    public void testFromBlobWithNullKeyValue() {
        transformer.fromBlob(null, null);
    }

    /**
     * Testing null value to "toBlob" method.
     */
    @Test(expected = TransformerSerDeException.class)
    public void testNullIgniteEventValue() {
        transformer.toBlob(null);
    }

    /**
     * testing json exception while converting event to bytes.
     *
     * @throws JsonProcessingException exception
     */
    @Test(expected = TransformerSerDeException.class)
    public void testJsonException() throws JsonProcessingException {

        transformer.toBlob(event);

    }

}
