// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.objectify;

import com.googlecode.objectify.ObjectifyFilter;
import javax.servlet.annotation.WebFilter;

/**
 * Objectify requires a filter to clean up any thread-local 
 * transaction contexts and pending asynchronous operations
 * that remain at the end of a request. This class hanles this by acting 
 * as a webFilter.
 */
@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {}