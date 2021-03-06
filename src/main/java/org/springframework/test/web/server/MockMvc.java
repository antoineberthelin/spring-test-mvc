/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.test.web.server;

import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/** <strong>Main entry point for server-side Spring MVC test support.</strong> */
public class MockMvc {

    private final ServletContext servletContext;

    private final MockMvcSetup mvcSetup;

    private boolean mapOnly;

    /** To create a {@link MockMvc} instance see methods in {@code MockMvcBuilders}. */
    MockMvc(ServletContext servletContext, MockMvcSetup mvcSetup) {
        this.servletContext = servletContext;
        this.mvcSetup = mvcSetup;
    }

    /**
     * Enables a mode in which requests are mapped to a handler without actually invoking it afterwards. Allows verifying
     * the handler or handler method a request is mapped to.
     */
    public MockMvc setMapOnly(boolean enable) {
        this.mapOnly = enable;
        return this;
    }

    /*
    public static MockMvc createFromApplicationContext(ApplicationContext applicationContext) {
        // TODO
        return null;
    }

    public static MockMvc createFromWebXml(String webXmlFileName) {
        // TODO
        return null;
    }
    */

    // Perform

    public MockMvcResultActionHelper perform(MockHttpServletRequestBuilder requestBuilder) {
        
    	MockHttpServletRequest request = requestBuilder.buildRequest(servletContext);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        final MockMvcResult result = MockMvcDispatcher.dispatch(request, response, mvcSetup, mapOnly);
		
        return new MockMvcResultActionHelper() {
			
			public MockMvcResultActionHelper andExpect(MockMvcResultMatcher matcher) {
				matcher.match(result);
				return this;
			}

			public void andPrintDebugInfo(MockMvcResultPrinter printer) {
				printer.print(result);
			}
			
		};
    }

}
