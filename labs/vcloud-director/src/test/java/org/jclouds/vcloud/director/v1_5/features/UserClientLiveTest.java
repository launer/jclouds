/*
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
package org.jclouds.vcloud.director.v1_5.features;

import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorClientLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests live behavior of {@link AdminGroupClient}.
 * 
 * @author danikov
 */
@Test(groups = { "live", "admin", "adminUser" }, singleThreaded = true, testName = "UserClientLiveTest")
public class UserClientLiveTest extends BaseVCloudDirectorClientLiveTest {
   
   public static final String GROUP = "admin group";

   /*
    * Convenience references to API clients.
    */

   /*
    * Shared state between dependant tests.
    */

   @Override
   @BeforeClass(inheritGroups = true)
   public void setupRequiredClients() {
   }
   
// POST /admin/org/{id}/users
   
// GET /admin/user/{id}
 
// PUT /admin/user/{id}
 
// DELETE /admin/user/{id}
 
// POST /admin/user/{id}/action/unlock
}