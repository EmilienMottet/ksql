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

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Immutable
public class StreamStreamJoin<K> implements ExecutionStep<KStreamHolder<K>> {

  private final ExecutionStepProperties properties;
  private final JoinType joinType;
  private final Formats leftFormats;
  private final Formats rightFormats;
  private final ExecutionStep<KStreamHolder<K>> left;
  private final ExecutionStep<KStreamHolder<K>> right;
  private final Duration before;
  private final Duration after;

  public StreamStreamJoin(
      final ExecutionStepProperties properties,
      final JoinType joinType,
      final Formats leftFormats,
      final Formats rightFormats,
      final ExecutionStep<KStreamHolder<K>> left,
      final ExecutionStep<KStreamHolder<K>> right,
      final Duration before,
      final Duration after) {
    this.properties = Objects.requireNonNull(properties, "properties");
    this.leftFormats = Objects.requireNonNull(leftFormats, "formats");
    this.rightFormats = Objects.requireNonNull(rightFormats, "rightFormats");
    this.joinType = Objects.requireNonNull(joinType, "joinType");
    this.left = Objects.requireNonNull(left, "left");
    this.right = Objects.requireNonNull(right, "right");
    this.before = Objects.requireNonNull(before, "before");
    this.after = Objects.requireNonNull(after, "after");
  }

  @Override
  public ExecutionStepProperties getProperties() {
    return properties;
  }

  @Override
  public List<ExecutionStep<?>> getSources() {
    return ImmutableList.of(left, right);
  }

  public Formats getLeftFormats() {
    return leftFormats;
  }

  public Formats getRightFormats() {
    return rightFormats;
  }

  public ExecutionStep<KStreamHolder<K>> getLeft() {
    return left;
  }

  public ExecutionStep<KStreamHolder<K>> getRight() {
    return right;
  }

  public JoinType getJoinType() {
    return joinType;
  }

  public Duration getAfter() {
    return after;
  }

  public Duration getBefore() {
    return before;
  }

  @Override
  public KStreamHolder<K> build(final PlanBuilder builder) {
    return builder.visitStreamStreamJoin(this);
  }

  // CHECKSTYLE_RULES.OFF: CyclomaticComplexity
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final StreamStreamJoin<?> that = (StreamStreamJoin<?>) o;
    return Objects.equals(properties, that.properties)
        && joinType == that.joinType
        && Objects.equals(leftFormats, that.leftFormats)
        && Objects.equals(rightFormats, that.rightFormats)
        && Objects.equals(left, that.left)
        && Objects.equals(right, that.right)
        && Objects.equals(before, that.before)
        && Objects.equals(after, that.after);
  }
  // CHECKSTYLE_RULES.ON: CyclomaticComplexity

  @Override
  public int hashCode() {
    return Objects.hash(
        properties,
        joinType,
        leftFormats,
        rightFormats,
        left,
        right,
        before,
        after
    );
  }
}
