/*
 * Copyright 2019 Confluent Inc.
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

package io.confluent.ksql.engine;

import io.confluent.ksql.execution.ddl.commands.DdlCommand;
import java.util.Objects;
import java.util.Optional;

public final class KsqlPlan {
  private final String statementText;
  private Optional<DdlCommand> ddlCommand;
  private Optional<QueryPlan> queryPlan;

  public KsqlPlan(
      final String statementText,
      final Optional<DdlCommand> ddlCommand,
      final Optional<QueryPlan> queryPlan
  ) {
    this.statementText = Objects.requireNonNull(statementText, "statementText");
    this.ddlCommand = Objects.requireNonNull(ddlCommand, "ddlCommand");
    this.queryPlan = Objects.requireNonNull(queryPlan, "queryPlan");
  }

  public Optional<DdlCommand> getDdlCommand() {
    return ddlCommand;
  }

  public Optional<QueryPlan> getQueryPlan() {
    return queryPlan;
  }

  public String getStatementText() {
    return statementText;
  }
}
