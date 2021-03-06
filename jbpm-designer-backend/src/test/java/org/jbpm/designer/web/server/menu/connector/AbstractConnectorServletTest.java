/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.jbpm.designer.web.server.menu.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.jbpm.designer.repository.Repository;
import org.jbpm.designer.repository.RepositoryBaseTest;
import org.jbpm.designer.web.profile.IDiagramProfile;
import org.jbpm.designer.web.profile.IDiagramProfileService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class AbstractConnectorServletTest extends RepositoryBaseTest {

    @Mock
    IDiagramProfileService profileService;

    @Spy
    @InjectMocks
    private AbstractConnectorServlet servlet = new AbstractConnectorServlet() {
        @Override
        protected void initializeDefaultRepo(IDiagramProfile profile, Repository repository, HttpServletRequest request) throws Exception {
            // do nothing
        }
    };

    private static final String FILE_NAME = "src/test/resources/designer.configuration";
    private static final String FILE_CONTENT = "application.context=/";

    @Before
    public void setup() {
        super.setup();
        when(profileService.findProfile(any(HttpServletRequest.class), anyString())).thenReturn(profile);
    }

    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    public void testGetBytesFromFileNullParameter() throws IOException {
        byte[] result = AbstractConnectorServlet.getBytesFromFile(null);
        assertNull(result);
    }

    @Test
    public void testGetBytesFromFile() throws  IOException {
        byte[] result = AbstractConnectorServlet.getBytesFromFile(new File(FILE_NAME));
        assertEquals(FILE_CONTENT, new String(result));
    }

    @Test
    public void testDoPostFindProfile() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        try {
            servlet.doPost(request, mock(HttpServletResponse.class));
        } catch (Exception e) {
            // exception thrown due to mocked request and response
        }
        verify(profileService, times(1)).findProfile(request, "jbpm");

    }

    @Test
    public void testDoPostProfileAlreadySet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        servlet.profile = profile;
        try {
            servlet.doPost(request, mock(HttpServletResponse.class));
        } catch (Exception e) {
            // exception thrown due to mocked request and response
        }
        verify(profileService, never()).findProfile(any(HttpServletRequest.class), anyString());

    }
}
