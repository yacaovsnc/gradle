/*
 * Copyright 2017 the original author or authors.
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
package org.gradle.composite.internal;

import org.gradle.api.internal.TaskInternal;

import java.util.function.Consumer;

public interface IncludedBuildController {
    void queueForExecution(String taskPath);

    IncludedBuildTaskResource.State getTaskState(String taskPath);

    TaskInternal getTask(String taskPath);

    boolean populateTaskGraph();

    void startTaskExecution();

    /**
     * Awaits completion of task execution, collecting any task failures into the given collection.
     */
    void awaitTaskCompletion(Consumer<? super Throwable> taskFailures);
}
