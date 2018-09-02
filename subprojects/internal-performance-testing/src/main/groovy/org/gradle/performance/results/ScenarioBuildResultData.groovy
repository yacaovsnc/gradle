/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance.results

import com.fasterxml.jackson.databind.ObjectMapper

class ScenarioBuildResultData {
    String scenarioName
    String webUrl
    boolean successful
    ExperimentData experimentData

    static class ExperimentData {
        String buildId
        String gitCommitId
        String controlGroupName
        String experimentGroupName
        String controlGroupMedian
        String experimentGroupMedian
        String controlGroupStandardError
        String experimentGroupStandardError
        String confidence
        double regressionPercentage
    }

    String getGitCommitId() {
        return experimentData?.gitCommitId
    }

    String getBuildId() {
        return experimentData?.buildId
    }

    String getControlGroupName() {
        return experimentData?.controlGroupName ?: "N/A"
    }

    String getExperimentGroupName() {
        return experimentData?.experimentGroupName ?: "N/A"
    }

    String getControlGroupMedian() {
        return experimentData?.controlGroupMedian ?: "N/A"
    }

    String getExperimentGroupMedian() {
        return experimentData?.experimentGroupMedian ?: "N/A"
    }

    String getControlGroupStandardError() {
        return experimentData?.controlGroupStandardError ?: "N/A"
    }

    String getExperimentGroupStandardError() {
        return experimentData?.experimentGroupStandardError ?: "N/A"
    }

    String getConfidence() {
        return experimentData?.confidence ?: "N/A"
    }

    double getRegressionPercentage() {
        return experimentData?.regressionPercentage ?: 0.0
    }

    boolean isRegressed() {
        return !successful && experimentData != null
    }

    void setResult(String junitSystemOut) {
        List<String> lines = junitSystemOut.readLines()
        List<Integer> startAndEndIndices = lines.findIndexValues { it.startsWith(BaselineVersion.MACHINE_DATA_SEPARATOR) }
        if (!startAndEndIndices.empty) {
            assert startAndEndIndices.size() == 2 && startAndEndIndices[0] + 2 == startAndEndIndices[1]
            String json = lines[startAndEndIndices[0].intValue() + 1]
            experimentData = new ObjectMapper().readValue(json, ExperimentData)
        }
    }

    String getFormattedRegression() {
        return experimentData ? String.format("%.2f%%", regressionPercentage) : "N/A"
    }
}
