/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

package io.druid.data.input.impl;

import com.google.common.collect.Lists;
import io.druid.java.util.common.parsers.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;

public class ParseSpecTest
{
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test(expected = ParseException.class)
  public void testDuplicateNames()
  {
    @SuppressWarnings("unused") // expected exception
    final ParseSpec spec = new DelimitedParseSpec(
        new TimestampSpec(
            "timestamp",
            "auto",
            null
        ),
        new DimensionsSpec(
            DimensionsSpec.getDefaultSchemas(Arrays.asList("a", "b", "a")),
            Lists.<String>newArrayList(),
            Lists.<SpatialDimensionSchema>newArrayList()
        ),
        ",",
        " ",
        Arrays.asList("a", "b"),
        false,
        0
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDimAndDimExcluOverlap()
  {
    @SuppressWarnings("unused") // expected exception
    final ParseSpec spec = new DelimitedParseSpec(
        new TimestampSpec(
            "timestamp",
            "auto",
            null
        ),
        new DimensionsSpec(
            DimensionsSpec.getDefaultSchemas(Arrays.asList("a", "B")),
            Lists.newArrayList("B"),
            Lists.<SpatialDimensionSchema>newArrayList()
        ),
        ",",
        null,
        Arrays.asList("a", "B"),
        false,
        0
    );
  }

  @Test
  public void testDimExclusionDuplicate()
  {
    @SuppressWarnings("unused") // expected exception
    final ParseSpec spec = new DelimitedParseSpec(
        new TimestampSpec(
            "timestamp",
            "auto",
            null
        ),
        new DimensionsSpec(
            DimensionsSpec.getDefaultSchemas(Collections.singletonList("a")),
            Lists.newArrayList("B", "B"),
            Lists.<SpatialDimensionSchema>newArrayList()
        ),
        ",",
        null,
        Arrays.asList("a", "B"),
        false,
        0
    );
  }

  @Test
  public void testDefaultTimestampSpec()
  {
    expectedException.expect(NullPointerException.class);
    expectedException.expectMessage("parseSpec requires timestampSpec");
    @SuppressWarnings("unused") // expected exception
    final ParseSpec spec = new DelimitedParseSpec(
        null,
        new DimensionsSpec(
            DimensionsSpec.getDefaultSchemas(Collections.singletonList("a")),
            Lists.newArrayList("B", "B"),
            Lists.<SpatialDimensionSchema>newArrayList()
        ),
        ",",
        null,
        Arrays.asList("a", "B"),
        false,
        0
    );
  }

  @Test
  public void testDimensionSpecRequired()
  {
    expectedException.expect(NullPointerException.class);
    expectedException.expectMessage("parseSpec requires dimensionSpec");
    @SuppressWarnings("unused") // expected exception
    final ParseSpec spec = new DelimitedParseSpec(
        new TimestampSpec(
            "timestamp",
            "auto",
            null
        ),
        null,
        ",",
        null,
        Arrays.asList("a", "B"),
        false,
        0
    );
  }
}
