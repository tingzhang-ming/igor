/*
 * Copyright 2016 Schibsted ASA.
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
 */

package com.netflix.spinnaker.igor.travis.service

import com.netflix.spinnaker.igor.build.model.GenericBuild
import com.netflix.spinnaker.igor.travis.client.model.Build
import com.netflix.spinnaker.igor.travis.client.model.Repo
import com.netflix.spinnaker.igor.travis.client.model.v3.V3Build
import groovy.transform.CompileStatic

@CompileStatic
class TravisBuildConverter {
    static GenericBuild genericBuild(Build build, String repoSlug, String baseUrl) {
        GenericBuild genericBuild = new GenericBuild(building: build.state == 'started', number: build.number, duration: build.duration, result: TravisResultConverter.getResultFromTravisState(build.state), name: repoSlug, url: url(repoSlug, baseUrl, build.id))
        if (build.finishedAt) {
            genericBuild.timestamp = build.timestamp()
        }
        return genericBuild
    }

    static GenericBuild genericBuild(V3Build build, String baseUrl) {
        GenericBuild genericBuild = new GenericBuild(building: build.state == 'started', number: build.number, duration: build.duration,result: TravisResultConverter.getResultFromTravisState(build.state),name: build.repository.slug,url: url(build.repository.slug, baseUrl, build.id))
        if (build.finishedAt) {
            genericBuild.timestamp = build.timestamp()
        }
        return genericBuild
    }

    static String url(String repoSlug, String baseUrl, int id) {
        "${baseUrl}/${repoSlug}/builds/${id}"
    }
}
