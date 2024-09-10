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

/**
 * This interface provides method to convert byte[] to IgniteKeys and viceversa.
 *
 * @author avadakkootko
 */
public interface IgniteKeyTransformer<T> {
    /**
     * Convert key to IgniteKey from byte[].
     *
     * @param key : byte[]
     * @return IgniteEvent : IgniteKey
     */
    public IgniteKey<T> fromBlob(byte[] key);

    /**
     * Convert key to byte[] from IgniteKey.
     *
     * @param key : IgniteKey
     * @return IgniteEvent : byte[]
     */
    public byte[] toBlob(IgniteKey<T> key);
}
