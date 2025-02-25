/*

  * Copyright (C) 2020-2022 Huawei Technologies Co., Ltd. All rights reserved.

  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.huaweicloud.sample.hessian;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hessian")
public class HessianController {
  @Autowired
  private HessianService hessianService;

  @RequestMapping("/testHessian")
  public String testHessian() {
    testBase();
    testChildBase();
    testGenericBase();
    testGenericChildBase();
    return "success";
  }

  private void testGenericChildBase() {
    ChildBase b = new ChildBase();
    b.setName("n");
    b.setAge("a");
    Generic<Base> g = new Generic<>();
    g.setData(Arrays.asList(b));
    Generic<Base> r = hessianService.generic(g);
    check(r.getData().get(0) instanceof Base, "wrong type");
    check(r.getData().get(0) instanceof ChildBase, "wrong type");
    check("n", r.getData().get(0).getName(), "wrong value");
    check("a", ((ChildBase) r.getData().get(0)).getAge(), "wrong value");
  }

  private void testGenericBase() {
    Base b = new Base();
    b.setName("n");
    Generic<Base> g = new Generic<>();
    g.setData(Arrays.asList(b));
    Generic<Base> r = hessianService.generic(g);
    check(r.getData().get(0) instanceof Base, "wrong type");
    check(!(r.getData().get(0) instanceof ChildBase), "wrong type");
    check("n", r.getData().get(0).getName(), "wrong value");
  }

  private void testBase() {
    Base b = new Base();
    b.setName("n");
    Base r = hessianService.base(b);
    check(r instanceof Base, "wrong type");
    check(!(r instanceof ChildBase), "wrong type");
    check("n", r.getName(), "wrong value");
  }

  private void testChildBase() {
    ChildBase b = new ChildBase();
    b.setName("n");
    b.setAge("a");
    Base r = hessianService.base(b);
    check(r instanceof Base, "wrong type");
    check(r instanceof ChildBase, "wrong type");
    check("n", r.getName(), "wrong value");
    check("a", ((ChildBase) r).getAge(), "wrong value");
  }

  private void check(boolean result, String error) {
    if (!result) {
      throw new RuntimeException(error);
    }
  }

  private void check(String expected, String actual, String error) {
    if (!StringUtils.equals(expected, actual)) {
      throw new RuntimeException(error);
    }
  }
}
