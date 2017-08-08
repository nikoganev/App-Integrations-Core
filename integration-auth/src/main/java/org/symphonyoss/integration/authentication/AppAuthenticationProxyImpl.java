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

package org.symphonyoss.integration.authentication;

import static org.symphonyoss.integration.authentication.properties.AppAuthenticationProxyProperties.UNREGISTERED_APP_MESSAGE;
import static org.symphonyoss.integration.authentication.properties.AppAuthenticationProxyProperties.UNREGISTERED_APP_SOLUTION;

import org.springframework.stereotype.Component;
import org.symphonyoss.integration.authentication.api.AppAuthenticationProxy;
import org.symphonyoss.integration.authentication.exception.UnregisteredAppAuthException;
import org.symphonyoss.integration.logging.LogMessageSource;
import org.symphonyoss.integration.model.yaml.IntegrationProperties;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;

/**
 * Perform the app authentication and keep the tokens for each application.
 *
 * Created by rsanchez on 07/08/17.
 */
@Component
public class AppAuthenticationProxyImpl implements AppAuthenticationProxy {

  private final Map<String, AppAuthenticationContext> appContexts = new HashMap<>();

  private final IntegrationProperties properties;

  private final LogMessageSource logMessage;

  public AppAuthenticationProxyImpl(IntegrationProperties properties, LogMessageSource logMessage) {
    this.properties = properties;
    this.logMessage = logMessage;
  }

  @Override
  public void registerApplication(String applicationId, KeyStore keyStore, String keyStorePassword) {
    AppAuthenticationContext context =
        new AppAuthenticationContext(applicationId, keyStore, keyStorePassword,
            properties.getApiClientConfig());

    appContexts.put(applicationId, context);
  }

  @Override
  public Client httpClientForApplication(String applicationId) {
    return contextForApplication(applicationId).httpClientForContext();
  }

  /**
   * Retrieves app authentication context.
   *
   * @param applicationId Application identifier
   * @return App authetication context
   * @throws UnregisteredAppAuthException Thrown when the application wasn't registered before it.
   */
  private AppAuthenticationContext contextForApplication(String applicationId) {
    AppAuthenticationContext context = this.appContexts.get(applicationId);

    if (context == null) {
      throw new UnregisteredAppAuthException(
          logMessage.getMessage(UNREGISTERED_APP_MESSAGE, applicationId),
          logMessage.getMessage(UNREGISTERED_APP_SOLUTION, applicationId));
    }

    return context;
  }
  
}
