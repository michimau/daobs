/**
 * Copyright 2014-2016 European Environment Agency
 *
 * Licensed under the EUPL, Version 1.1 or – as soon
 * they will be approved by the European Commission -
 * subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance
 * with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.daobs.controller;

import org.daobs.api.exception.ResourceNotFoundException;
import org.daobs.indicator.config.Indicator;
import org.daobs.indicator.config.Reporting;
import org.daobs.indicator.config.Variable;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by francois on 17/10/14.
 */
public interface IndicatorCalculator {
  /**
   * Load or reload the configuration.
   *
   */
  IndicatorCalculator loadConfig() throws FileNotFoundException;

  Reporting getConfiguration();

  /**
   * Add or update an indicator.
   */
  IndicatorCalculator addIndicator(Indicator indicator);

  /**
   * Remove an indicator.
   */
  IndicatorCalculator removeIndicator(String indicatorId) throws ResourceNotFoundException;

  /**
   * Add or update a variable.
   */
  IndicatorCalculator addVariable(Variable variable);

  /**
   * Remove a variable.
   */
  IndicatorCalculator removeVariable(String variableId) throws ResourceNotFoundException;

  Double get(String indicatorName);

  /**
   * Compute indicators.
   *
   */
  IndicatorCalculator computeIndicators(String scopeId, String date, String... filterQuery);

  Map<String, Double> getResults();
}
