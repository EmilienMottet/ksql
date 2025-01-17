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

package io.confluent.ksql.rest.server.validation;

import io.confluent.ksql.KsqlExecutionContext;
import io.confluent.ksql.parser.tree.TerminateQuery;
import io.confluent.ksql.query.QueryId;
import io.confluent.ksql.services.ServiceContext;
import io.confluent.ksql.statement.ConfiguredStatement;
import io.confluent.ksql.util.KsqlStatementException;
import java.util.Map;

public final class TerminateQueryValidator {

  private TerminateQueryValidator() { }

  public static void validate(
      final ConfiguredStatement<?> statement,
      final Map<String, ?> sessionProperties,
      final KsqlExecutionContext context,
      final ServiceContext serviceContext
  ) {
    final TerminateQuery terminateQuery = (TerminateQuery) statement.getStatement();
    final QueryId queryId = terminateQuery.getQueryId();

    context.getPersistentQuery(queryId)
        .orElseThrow(() -> new KsqlStatementException(
            "Unknown queryId: " + queryId,
            statement.getStatementText()))
        .close();
  }

}
