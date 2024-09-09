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

import com.harman.ignite.domain.DeviceMessageFailureEventDataV1_0;
import com.harman.ignite.domain.EventAttribute;
import com.harman.ignite.domain.IgniteEventSource;
import com.harman.ignite.domain.Version;
import com.harman.ignite.entities.IgniteEventImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;



/** Test class for DeviceMessageIgniteEventTransformer.
 *
 * @see DeviceMessageIgniteEventTransformer
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { com.harman.ignite.transform.config.TransformerTestConfig.class })
public class DeviceMessageIgniteEventTransformerTest {

    private static final int TEN = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessageIgniteEventTransformerTest.class);

    @Autowired
    private DeviceMessageIgniteEventTransformer transformer;

    @Autowired
    private GenericIgniteEventTransformer genericTransformer;

    @Test(expected = RuntimeException.class)
    public void testFromBlob() {
        transformer.fromBlob(null, null);
    }

    @Test
    public void testToBlob() {

        IgniteEventImpl event = new IgniteEventImpl();
        TestEventData data = new TestEventData(TEN);
        event.setEventId("Speed");
        event.setVersion(Version.V1_0);
        event.setBizTransactionId("Biz1234");
        event.setRequestId("Req1234");
        event.setVehicleId("Vehicle1234");
        event.setEventData(data);
        byte[] eventAsByteArray = transformer.toBlob(event);
        String converted = new String(eventAsByteArray);
        // Should not contain REQUEST_ID
        boolean containsRequestId = converted.contains(EventAttribute.REQUEST_ID);
        Assert.assertFalse(containsRequestId);
        // Should not contain VEHICLE_ID
        boolean containsVehicleId = converted.contains(EventAttribute.VEHICLE_ID);
        Assert.assertFalse(containsVehicleId);
        boolean containsDeviceDeliveryCutOff = converted.contains(EventAttribute.DEVICE_DELIVERY_CUTOFF);
        Assert.assertFalse(containsDeviceDeliveryCutOff);
        String dataExpected = "\"Data\":{\"retryAttempts\":10}\"";
        // EventData should not been filtered out even though filter has been
        // added
        boolean containsEventData = converted.contains(dataExpected);
        Assert.assertFalse(containsEventData);

    }

    @Test
    public void deviceMessageFailureFeedback() {
        String jsonStr = "{\"EventID\":\"DeviceMessageFailure\",\"Version\":\"1.0\",\"Timestamp\":1534754788012,"
                + "\"Data\":{\"failedIgniteEvent\":{\"EventID\":\"Speed\",\"Version\":\"1.0\","
                + "\"Timestamp\":1534754788012,\"Data\":{\"value\":20.0},\"SourceDeviceId\":\"Device12345\","
                + "\"VehicleId\":\"Vehicle12345\",\"Timezone\":0,\"MessageId\":\"1237\",\""
                + "BizTransactionId\":\"Biz1237\",\"DeviceDeliveryCutoff\":-1},"
                + "\"retryAttempts\":0,\"shoudlerTapRetryAttempts\":0,"
                + "\"deviceDeliveryCutoffExceeded\":false,\"deviceStatusInactive\":false,"
                + "\"errorCode\":\"DEVICE_STATUS_INACTIVE\"},"
                + "\"Timezone\":0,\"MessageId\":\"214651\",\"BizTransactionId\":\"Biz1237\""
                + ",\"DeviceDeliveryCutoff\":-1}";
        IgniteEventImpl event = (IgniteEventImpl) genericTransformer.fromBlob(jsonStr.getBytes(), Optional.empty());
        Assert.assertEquals("DeviceMessageFailure", event.getEventId());
        DeviceMessageFailureEventDataV1_0 failEventData = (DeviceMessageFailureEventDataV1_0) event.getEventData();
        Assert.assertEquals("Speed", failEventData.getFailedIgniteEvent().getEventId());
    }

    @Test
    public void testGetSource() {
        Assert.assertEquals(IgniteEventSource.IGNITE, transformer.getSource());
    }

}
