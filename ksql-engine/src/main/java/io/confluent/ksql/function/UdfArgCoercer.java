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

package io.confluent.ksql.function;

import com.google.common.primitives.Primitives;
import java.lang.reflect.Array;

final class UdfArgCoercer {

  private UdfArgCoercer() {
  }

  static <T> T coerceUdfArgs(
      final Object[] args,
      final Class<? extends T> clazz,
      final int index) {
    final Object arg = args[index];
    return coerceUdfArgs(arg, clazz, index);
  }

  static <T> T coerceUdfArgs(
      final Object arg,
      final Class<? extends T> clazz,
      final int index
  ) {
    if (arg == null) {
      if (clazz.isPrimitive()) {
        throw new KsqlFunctionException(
            String.format(
                "Can't coerce argument at index %d from null to a primitive type", index));
      }
      return null;
    }

    if (clazz.isArray()) {
      try {
        return fromArray(arg, clazz);
      } catch (Exception e) {
        throw new KsqlFunctionException(
            String.format("Couldn't coerce array argument \"args[%d]\" to type %s", index, clazz)
        );
      }
    }

    // using boxed type is safe: long.class and Long.class are both of type Class<Long>
    // and this is a no-op for non-primitives
    final Class<? extends T> boxedType = Primitives.wrap(clazz);
    if (boxedType.isAssignableFrom(arg.getClass())) {
      return boxedType.cast(arg);
    } else if (arg instanceof String) {
      try {
        return fromString((String) arg, clazz);
      } catch (Exception e) {
        throw new KsqlFunctionException(
            String.format("Couldn't coerce string argument '\"args[%d]\"' to type %s",
                index, clazz));
      }
    } else if (arg instanceof Number) {
      try {
        return fromNumber((Number) arg, boxedType);
      } catch (Exception e) {
        throw new KsqlFunctionException(
            String.format("Couldn't coerce numeric argument '\"args[%d]:(%s) %s\"' to type %s",
                index, arg.getClass(), arg, clazz));
      }
    } else {
      throw new KsqlFunctionException(
          String.format("Impossible to coerce (%s) %s into %s", arg.getClass(), arg, clazz));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T fromArray(
      final Object args,
      final Class<? extends T> arrayType
  ) {
    if (!args.getClass().isArray()) {
      throw new KsqlFunctionException(
          String.format("Cannot coerce non-array object %s to %s", args, arrayType));
    }

    final int length = Array.getLength(args);
    final Class<?> componentType = arrayType.getComponentType();
    final Object val = Array.newInstance(componentType, length);
    for (int i = 0; i < length; i++) {
      Array.set(val, i, coerceUdfArgs(Array.get(args, i), componentType, i));
    }
    return (T) val;
  }

  private static <T> T fromNumber(final Number arg, final Class<? extends T> boxedType) {
    if (Integer.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.intValue());
    } else if (Long.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.longValue());
    } else if (Double.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.doubleValue());
    } else if (Float.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.floatValue());
    } else if (Byte.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.byteValue());
    } else if (Short.class.isAssignableFrom(boxedType)) {
      return boxedType.cast(arg.shortValue());
    }

    throw new KsqlFunctionException(String.format("Cannot coerce %s into %s", arg, boxedType));
  }

  @SuppressWarnings("unchecked")
  private static <T> T fromString(final String arg, final Class<T> clazz) {
    if (Integer.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Integer.valueOf(arg);
    } else if (Long.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Long.valueOf(arg);
    } else if (Double.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Double.valueOf(arg);
    } else if (Float.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Float.valueOf(arg);
    } else if (Byte.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Byte.valueOf(arg);
    } else if (Short.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Short.valueOf(arg);
    } else if (Boolean.class.isAssignableFrom(Primitives.wrap(clazz))) {
      return (T) Boolean.valueOf(arg);
    }

    throw new KsqlFunctionException(String.format("Cannot coerce %s into %s", arg, clazz));
  }

}