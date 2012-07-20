/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.trystack.nova.features;

import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApiLiveTest;
import org.testng.annotations.Test;

/**
 * 
 * @author Michael Arnold
 */
@Test(groups = "live", testName = "TryStackNovaKeyPairApiLiveTest")
public class TryStackNovaKeyPairApiLiveTest extends KeyPairApiLiveTest {
   public TryStackNovaKeyPairApiLiveTest() {
      provider = "trystack-nova";
   }

}