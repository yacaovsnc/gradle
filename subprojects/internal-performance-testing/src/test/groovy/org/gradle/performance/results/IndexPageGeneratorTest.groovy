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

import groovy.json.JsonOutput
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

class IndexPageGeneratorTest extends Specification {
    @Rule
    TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    @Subject
    IndexPageGenerator generator

    File resultsJson = tmpDir.file('results.json')

    def setup() {
        resultsJson << "[]"
        generator = new IndexPageGenerator(resultsJson)
    }

    String regressionOutput(double regressionPercentage) {
        """
${BaselineVersion.MACHINE_DATA_SEPARATOR}
${JsonOutput.toJson([regressionPercentage: regressionPercentage])}
${BaselineVersion.MACHINE_DATA_SEPARATOR}
        """
    }

    def 'can sort scenarios correctly'() {
        resultsJson.text = JsonOutput.toJson([
            new ScenarioBuildResultData(scenarioName: 'no regression', webUrl: 'no regression url', successful: true),
            new ScenarioBuildResultData(scenarioName: 'small regression', webUrl: 'small regression url', successful: false, result: regressionOutput(1.0)),
            new ScenarioBuildResultData(scenarioName: 'big regression', webUrl: 'big regression url', successful: false, result: regressionOutput(10.0)),
            new ScenarioBuildResultData(scenarioName: 'build failure', webUrl: 'build failure url', successful: false),
        ])

        expect:
        generator.readBuildResultData(resultsJson).toList().collect { it.scenarioName } == ['build failure', 'big regression', 'small regression', 'no regression']
    }
}
