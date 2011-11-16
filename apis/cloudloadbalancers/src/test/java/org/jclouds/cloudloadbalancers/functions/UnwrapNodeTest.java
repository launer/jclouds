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
package org.jclouds.cloudloadbalancers.functions;

import org.jclouds.cloudloadbalancers.domain.Node;
import org.jclouds.cloudloadbalancers.domain.Node.Status;
import org.jclouds.cloudloadbalancers.domain.internal.BaseNode.Condition;
import org.jclouds.http.HttpResponse;
import org.jclouds.json.BaseItemParserTest;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.inject.Injector;

/**
 * 
 * @author Dan Lo Bianco
 */
@Test(groups = "unit")
public class UnwrapNodeTest extends BaseItemParserTest<Node> {

   @Override
   public String resource() {
      return "/getnode.json";
   }

   @Override
   public Node expected() {
      return Node.builder().id(410).address("10.1.1.1").port(80)
      	.condition(Condition.ENABLED).status(Status.ONLINE).weight(12).build();
   }

   @Override
   protected Function<HttpResponse, Node> parser(Injector i) {
      return i.getInstance(UnwrapNode.class);
   }
}
