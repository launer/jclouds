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
package org.jclouds.trmk.ecloud;

import static org.jclouds.trmk.vcloud_0_8.options.InstantiateVAppTemplateOptions.Builder.processorCount;

import java.util.Properties;
import java.util.Map.Entry;

import org.jclouds.domain.Credentials;
import org.jclouds.net.IPSocket;
import org.jclouds.ssh.SshClient;
import org.jclouds.trmk.ecloud.suppliers.TerremarkECloudInternetServiceAndPublicIpAddressSupplier;
import org.jclouds.trmk.vcloud_0_8.TerremarkClientLiveTest;
import org.jclouds.trmk.vcloud_0_8.domain.InternetService;
import org.jclouds.trmk.vcloud_0_8.domain.Protocol;
import org.jclouds.trmk.vcloud_0_8.domain.PublicIpAddress;
import org.jclouds.trmk.vcloud_0_8.domain.VApp;
import org.jclouds.trmk.vcloud_0_8.options.InstantiateVAppTemplateOptions;
import org.jclouds.trmk.vcloud_0_8.reference.VCloudConstants;
import org.testng.annotations.Test;

/**
 * Tests behavior of {@code TerremarkECloudClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", enabled = true, singleThreaded = true, testName = "TerremarkECloudClientLiveTest")
public class TerremarkECloudClientLiveTest extends TerremarkClientLiveTest {

   public TerremarkECloudClientLiveTest() {
      this.provider = "trmk-ecloud";
      this.itemName = "Ubuntu Server 10.04 x64";
      this.expectedOs = "Ubuntu Linux (64-bit)";
   }

   @Override
   protected Properties setupProperties() {
      Properties props = super.setupProperties();
      props.setProperty(VCloudConstants.PROPERTY_VCLOUD_DEFAULT_VDC,
            ".* - " + System.getProperty("test.trmk-ecloud.datacenter", "MIA"));
      return props;
   }

   @Override
   protected InstantiateVAppTemplateOptions createInstantiateOptions() {
      return processorCount(1).memory(512);
   }

   @Override
   protected SshClient getConnectionFor(IPSocket socket) {
      return sshFactory.create(socket, new Credentials("ecloud", "$Ep455l0ud!2"));
   }

   @Override
   protected Entry<InternetService, PublicIpAddress> getNewInternetServiceAndIpForSSH(VApp vApp) {
      return new TerremarkECloudInternetServiceAndPublicIpAddressSupplier(TerremarkECloudClient.class.cast(connection))
            .getNewInternetServiceAndIp(vApp, 22, Protocol.TCP);
   }

}
