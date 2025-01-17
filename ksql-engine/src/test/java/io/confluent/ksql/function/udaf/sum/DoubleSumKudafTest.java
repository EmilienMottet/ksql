/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.function.udaf.sum;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import io.confluent.ksql.function.AggregateFunctionInitArguments;
import io.confluent.ksql.function.KsqlAggregateFunction;
import java.util.Collections;
import org.apache.kafka.connect.data.Schema;

public class DoubleSumKudafTest extends BaseSumKudafTest<Double, DoubleSumKudaf> {
  protected TGenerator<Double> getNumberGenerator() {
    return Double::valueOf;
  }

  protected DoubleSumKudaf getSumKudaf() {
    final KsqlAggregateFunction aggregateFunction = new SumAggFunctionFactory()
        .createAggregateFunction(Collections.singletonList(Schema.OPTIONAL_FLOAT64_SCHEMA),
            AggregateFunctionInitArguments.EMPTY_ARGS);
    assertThat(aggregateFunction, instanceOf(DoubleSumKudaf.class));
    return  (DoubleSumKudaf) aggregateFunction;
  }
}
