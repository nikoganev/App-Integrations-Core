/**
 * Copyright 2016-2017 Symphony Integrations - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.integration.auth.api.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.symphonyoss.integration.auth.api.exception.AuthUrlNotFoundException;
import org.symphonyoss.integration.model.yaml.IntegrationProperties;

/**
 * Unit test for {@link KmAuthHttpApiClient}
 * Created by rsanchez on 23/02/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class KmAuthHttpApiClientTest {

  @Mock
  private IntegrationProperties properties;

  @InjectMocks
  private KmAuthHttpApiClient kmAuthHttpApiClient = new KmAuthHttpApiClient();

  @Test(expected = AuthUrlNotFoundException.class)
  public void testInitFail() {
    kmAuthHttpApiClient.init();

    assertNull(kmAuthHttpApiClient.getClient());
  }

  @Test
  public void testInit() {
    doReturn("https://km.symphony.com/sessionauth").when(properties).getKeyManagerAuthUrl();

    kmAuthHttpApiClient.init();
    assertNotNull(kmAuthHttpApiClient.getClient());
  }
}
