/**
 * Copyright 2018 The original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package io.dekorate.docker.generator;

import java.util.Map;

import io.dekorate.Generator;
import io.dekorate.Session;
import io.dekorate.WithProject;
import io.dekorate.WithSession;
import io.dekorate.config.AnnotationConfiguration;
import io.dekorate.config.ConfigurationSupplier;
import io.dekorate.config.PropertyConfiguration;
import io.dekorate.docker.adapter.DockerBuildConfigAdapter;
import io.dekorate.docker.config.DockerBuildConfig;
import io.dekorate.kubernetes.config.Configuration;
import io.dekorate.kubernetes.configurator.ApplyBuildToImageConfiguration;
import io.dekorate.project.ApplyProjectInfo;

public interface DockerBuildGenerator extends Generator, WithSession, WithProject {

  default String getKey() {
    return "docker";
  }

  default Class<? extends Configuration> getConfigType() {
    return DockerBuildConfig.class;
  }

  @Override
  default void addAnnotationConfiguration(Map map) {
    on(new AnnotationConfiguration<DockerBuildConfig>(
        DockerBuildConfigAdapter.newBuilder(propertiesMap(map, DockerBuildConfig.class))
            .accept(new ApplyProjectInfo(getProject()))
            .accept(new ApplyBuildToImageConfiguration())));
  }

  @Override
  default void addPropertyConfiguration(Map map) {
    on(new PropertyConfiguration<DockerBuildConfig>(
        DockerBuildConfigAdapter.newBuilder(propertiesMap(map, DockerBuildConfig.class))
            .accept(new ApplyProjectInfo(getProject()))
            .accept(new ApplyBuildToImageConfiguration())));
  }

  default void on(ConfigurationSupplier<DockerBuildConfig> config) {
    Session session = getSession();
    session.configurators().add(config);
  }
}
