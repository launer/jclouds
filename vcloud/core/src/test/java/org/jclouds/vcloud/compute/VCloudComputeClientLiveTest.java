/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.vcloud.compute;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.Constants.PROPERTY_TRUST_ALL_CERTS;
import static org.jclouds.vcloud.options.InstantiateVAppTemplateOptions.Builder.processorCount;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.jclouds.compute.domain.OsFamily;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.net.IPSocket;
import org.jclouds.rest.RestContextFactory;
import org.jclouds.vcloud.VCloudClient;
import org.jclouds.vcloud.domain.Status;
import org.jclouds.vcloud.domain.VApp;
import org.jclouds.vcloud.domain.VAppTemplate;
import org.jclouds.vcloud.options.InstantiateVAppTemplateOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code VCloudClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", enabled = true, sequential = true, testName = "vcloud.VCloudClientLiveTest")
public class VCloudComputeClientLiveTest {
   protected String provider;
   protected VCloudComputeClient computeClient;
   protected VCloudClient client;

   protected URI id;
   protected String publicAddress;
   protected String templateName;

   public static class Expectation {
      final long hardDisk;
      final String os;

      public Expectation(long hardDisk, String os) {
         this.hardDisk = hardDisk;
         this.os = os;
      }
   }

   protected Map<OsFamily, Expectation> expectationMap;

   protected Predicate<IPSocket> addressTester;
   private String identity;
   private String credential;

   @Test(enabled = true)
   public void testPowerOn() throws InterruptedException, ExecutionException, TimeoutException, IOException {
      OsFamily toTest = OsFamily.CENTOS;

      String serverName = getCompatibleServerName(toTest);
      int processorCount = 1;
      int memory = 512;

      VAppTemplate template = client.findVAppTemplateInOrgCatalogNamed(null, null, templateName);
      InstantiateVAppTemplateOptions options = processorCount(1).memory(512).disk(10 * 1025 * 1024);

      id = URI.create(computeClient.start(null, template.getHref(), templateName, options).get("id"));
      Expectation expectation = expectationMap.get(toTest);

      VApp vApp = client.getVApp(id);
      verifyConfigurationOfVApp(vApp, serverName, expectation.os, processorCount, memory, expectation.hardDisk);
      assertEquals(vApp.getStatus(), Status.ON);
   }

   private String getCompatibleServerName(OsFamily toTest) {
      String serverName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, toTest.toString()).substring(0,
               toTest.toString().length() <= 15 ? toTest.toString().length() : 14);
      return serverName;
   }

   @Test(dependsOnMethods = "testPowerOn", enabled = true)
   public void testGetPublicAddresses() {
      publicAddress = Iterables.getLast(computeClient.getPublicAddresses(id));
      assert !addressTester.apply(new IPSocket(publicAddress, 22));
   }

   private void verifyConfigurationOfVApp(VApp vApp, String serverName, String expectedOs, int processorCount,
            int memory, long hardDisk) {
      assertEquals(vApp.getName(), serverName);
      // assertEquals(vApp.getOperatingSystemDescription(), expectedOs);
      // assertEquals(Iterables
      // .getOnlyElement(filter(vApp.getResourceAllocations(),
      // resourceType(ResourceType.PROCESSOR)))
      // .getVirtualQuantity(), processorCount);
      // assertEquals(Iterables.getOnlyElement(
      // filter(vApp.getResourceAllocations(),
      // resourceType(ResourceType.SCSI_CONTROLLER))).getVirtualQuantity(),
      // 1);
      // assertEquals(Iterables.getOnlyElement(filter(vApp.getResourceAllocations(),
      // resourceType(ResourceType.MEMORY)))
      // .getVirtualQuantity(), memory);
      // assertEquals(Iterables.getOnlyElement(
      // filter(vApp.getResourceAllocations(),
      // resourceType(ResourceType.DISK_DRIVE))).getVirtualQuantity(),
      // hardDisk);
   }

   @AfterTest
   void cleanup() throws InterruptedException, ExecutionException, TimeoutException {
      if (id != null)
         computeClient.stop(id);
   }

   protected void setupCredentials() {
      identity = checkNotNull(System.getProperty("vcloud.identity"), "vcloud.identity");
      credential = checkNotNull(System.getProperty("vcloud.credential"), "vcloud.credential");
   }

   @BeforeGroups(groups = { "live" })
   public void setupClient() {
      setupCredentials();
      Properties properties = new Properties();
      properties.setProperty(PROPERTY_TRUST_ALL_CERTS, "true");
      Injector injector = new RestContextFactory().createContextBuilder("vcloud", identity, credential,
               ImmutableSet.<Module> of(new Log4JLoggingModule()), properties).buildInjector();

      computeClient = injector.getInstance(VCloudComputeClient.class);
      client = injector.getInstance(VCloudClient.class);
      addressTester = injector.getInstance(Key.get(new TypeLiteral<Predicate<IPSocket>>() {
      }));
      expectationMap = ImmutableMap.<OsFamily, Expectation> builder().put(OsFamily.CENTOS,
               new Expectation(4194304 / 2 * 10, "Red Hat Enterprise Linux 5 (64-bit)")).build();
      provider = "vcloudtest";
      templateName = "Ubuntu JeOS 9.10 (32-bit)";
   }

}
