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

import com.harman.ignite.key.IgniteKey;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing IgniteKeyTransformer.
 */
public class IgniteKeyTransformerTest {

    /**
     * Testing fromBlob method of IgniteStringKey.
     */
    @Test
    public void testIgniteKeyFromBlob() {
        String str = "test";

        IgniteKeyTransformerStringImpl igniteKeyTransformer = new IgniteKeyTransformerStringImpl();

        IgniteKey<String> igniteKey = igniteKeyTransformer.fromBlob(str.getBytes());

        Assert.assertNotNull(igniteKey);
        Assert.assertEquals(str, igniteKey.getKey());

    }

    /**
     * Testing toBlob method of IgniteStringKey.
     */
    @Test
    public void testIgniteKeyToBlob() {

        String str = "test";

        IgniteKeyTransformerStringImpl igniteKeyTransformer = new IgniteKeyTransformerStringImpl();

        IgniteKey<String> key = new IgniteKey<String>() {

            @Override
            public String getKey() {
                return str;
            }
        };

        byte[] keyBytes = igniteKeyTransformer.toBlob(key);

        Assert.assertArrayEquals(str.getBytes(), keyBytes);

    }

}
