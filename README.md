# Quarkus Rapid API Key Authentication Mechanism

This Quarkus project implements a custom authentication mechanism based on the RapidAPI key for securing your application. The mechanism allows you to authenticate incoming requests based on the presence and validity of a specific HTTP header named `X-RapidAPI-Proxy-Secret`.

## Overview

This authentication mechanism is implemented as an alternative HTTP authentication mechanism in Quarkus. It checks if the `X-RapidAPI-Proxy-Secret` header in the incoming request matches the configured RapidAPI key. If it matches, the request is considered authenticated, and a `QuarkusSecurityIdentity` is created. If the key does not match, an unauthorized challenge is issued.

## Configuration

To configure this authentication mechanism, you need to set the RapidAPI key in your application configuration. You can do this in the `application.properties` or `application.yml` file or by using environment variables. Ensure that you specify the `RAPID_API_KEY` property:

```properties
RAPID_API_KEY=your-rapid-api-key
```

## Usage

To use this authentication mechanism in your Quarkus application, you should do the following:

   * Include the RapidApiKeyAuthentication class in your project.
   * Ensure that the RAPID_API_KEY property is set to the correct RapidAPI key in your application configuration.

## Implementation Details

## Implementation Details

The `RapidApiKeyAuthentication` class implements the `HttpAuthenticationMechanism` interface and is responsible for authenticating requests based on the RapidAPI key. Here are the key details of the implementation:

- `authenticate`: This method checks if the `X-RapidAPI-Proxy-Secret` header in the incoming request matches the configured RapidAPI key. If there is a match, a `QuarkusSecurityIdentity` is created and returned, indicating a successful authentication. If the key does not match, the method returns `null`, indicating an authentication failure.

- `getChallenge`: This method is responsible for returning an unauthorized challenge in case of authentication failure. It provides the appropriate HTTP status code and the `X-RapidAPI-Proxy-Secret` header for the challenge.

- `getCredentialTypes`: This method specifies the credential type that this authentication mechanism is capable of processing. In this case, it handles `ApiKeyCredentialTypes.class`.

- `getCredentialTransport`: This method specifies the transport type used for credentials. It defines an HTTP header named `X-RapidAPI-Proxy-Secret` for credential transport.

These methods work together to secure your application by checking the RapidAPI key in incoming requests and providing authentication or challenges as necessary.

```java
// Example of using the authenticate method in a Quarkus endpoint
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getSecureData() {
    // Your secured resource logic goes here
}
```
## Contact
For any questions or issues, please feel free to open an issue on our GitHub repository.