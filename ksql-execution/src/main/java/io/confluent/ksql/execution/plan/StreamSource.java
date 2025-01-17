/*
 * Copyright 2019 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.execution.plan;

import com.google.errorprone.annotations.Immutable;
import io.confluent.ksql.schema.ksql.LogicalSchema;
import io.confluent.ksql.util.timestamp.TimestampExtractionPolicy;
import java.util.Optional;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.streams.Topology.AutoOffsetReset;

@Immutable
public final class StreamSource extends AbstractStreamSource<KStreamHolder<Struct>> {
  public StreamSource(
      final ExecutionStepProperties properties,
      final String topicName,
      final Formats formats,
      final TimestampExtractionPolicy timestampPolicy,
      final int timestampIndex,
      final Optional<AutoOffsetReset> offsetReset,
      final LogicalSchema sourceSchema) {
    super(
        properties,
        topicName,
        formats,
        timestampPolicy,
        timestampIndex,
        offsetReset,
        sourceSchema
    );
  }

  @Override
  public KStreamHolder<Struct> build(final PlanBuilder builder) {
    return builder.visitStreamSource(this);
  }
}
