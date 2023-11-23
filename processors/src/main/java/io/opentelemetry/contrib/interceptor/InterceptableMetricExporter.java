/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.contrib.interceptor;

import io.opentelemetry.contrib.interceptor.api.Interceptor;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.util.Collection;

/** Intercepts metrics before delegating them to the real exporter. */
public final class InterceptableMetricExporter implements MetricExporter {
  private final MetricExporter delegate;
  private final Interceptor<MetricData> interceptor;

  public InterceptableMetricExporter(MetricExporter delegate, Interceptor<MetricData> interceptor) {
    this.delegate = delegate;
    this.interceptor = interceptor;
  }

  @Override
  public CompletableResultCode export(Collection<MetricData> metrics) {
    return delegate.export(interceptor.interceptAll(metrics));
  }

  @Override
  public CompletableResultCode flush() {
    return delegate.flush();
  }

  @Override
  public CompletableResultCode shutdown() {
    return delegate.shutdown();
  }

  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return delegate.getAggregationTemporality(instrumentType);
  }
}
