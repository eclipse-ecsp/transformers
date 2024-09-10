[<img src="./images/logo.png" width="400" height="200"/>](./images/logo.png)

# Transformers
[![Build](https://github.com/HARMAN-Automotive/transformers/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/HARMAN-Automotive/transformers/actions/workflows/maven-publish.yml)
[![License Compliance](https://github.com/Harman-Automotive/transformers/actions/workflows/license-compliance.yml/badge.svg)](https://github.com/Harman-Automotive/transformers/actions/workflows/license-compliance.yml)

The `transformers` project provides serialization/deserialization support to the services. The following approaches are supported:

1. Serialization/Deserialization support for an `BlobEvent`.
2. Use of [Fast Serialization](https://github.com/RuedigerMoeller/fast-serialization/tree/master) library to implement FST based serialization/deserialization for ingestion data
3. Below Transformers along with the capability of extending `Transformer` to create a new transformer for event
   - Device Message Transformer - serialization to `byte[]` from event
   - Generic Transformer - serialization/deserialization to/from `byte[]` to generic event along with event attributes validation
   - Event data key transformer - serialization/deserialization to/from `byte[]` to key
4. Support for adding object based serializers, deserializers and subtypes to the Jackson module's `ObjectMapper`

# Table of Contents
* [Getting Started](#getting-started)
* [Usage](#usage)
* [How to contribute](#how-to-contribute)
* [Built with Dependencies](#built-with-dependencies)
* [Code of Conduct](#code-of-conduct)
* [Authors](#authors)
* [Security Contact Information](#security-contact-information)
* [Support](#support)
* [Troubleshooting](#troubleshooting)
* [License](#license)
* [Announcements](#announcements)


## Getting Started

To build the project in the local working directory after the project has been cloned/forked, run:

```mvn clean install```

from the command line interface.

### Prerequisites

1. Maven
2. Java 17

### Installation

[How to set up maven](https://maven.apache.org/install.html)

[Install Java](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)

### Running the tests

```mvn test```

Or run a specific test

```mvn test -Dtest="TheFirstUnitTest"```

To run a method from within a test

```mvn test -Dtest="TheSecondUnitTest#whenTestCase2_thenPrintTest2_1"```

### Deployment

`Transformers` project serves as a library for the services. It is not meant to be deployed as a service in any cloud environment.

## Usage
Add the following dependency in the target project
```
<dependency>
  <groupId>com.harman.ignite</groupId>
  <artifactId>ignite-transformers</artifactId>
  <version>3.X.X</version>
</dependency>

```

### Implementing a Transformer

A custom transformer can be created for an `IgniteEvent` type by extending the `Transformer` contract.

Example:

```java
public class GenericIgniteEventTransformer implements Transformer {
    @Override
    public IgniteEvent fromBlob(byte[] value, Optional<IgniteEventBase> header) {
        // deserialization logic
    }

    @Override
    public byte[] toBlob(IgniteEvent value) {
        // serialization logic
    }
}
```

### Custom serializers, deserializers and subtypes

Services can provide custom serializers, deserializers, and subtypes to the Jackson's object mapper from the environment properties specified by the following:

```properties
#comma separated value for custom deserializer. className and it's deserializer needs to separated by :
#Example: k1:v1,k2:v2
custom.deserializers=com.harman.ignite.entities.EventData:com.harman.ignite.entities.EventDataDeSerializer
#Custom serializer in the form of  k1:v1,k2:v2
custom.serializers=
#Custom subtypes in the form of k1:v1,k2:v2
custom.subtypes=
```

### Implementing `IgniteEvent` attribute validation

The validation for a particular `IgniteEvent` attribute needs to be configured in the environment properties by the service.

> **_NOTE:_** attribute name needs to be followed with 'inputValidation' prefix

Example:

```properties
EventID.inputvalidation=ALPHA
BizTransactionId.inputvalidation=ALPHA_NUMERIC
Timestamp.inputvalidation=NUMERIC|13
DFFQualifier.inputvalidation=ALPHA
CorrelationId.inputvalidation=NUMERIC
MessageId.inputvalidation=NUMERIC|6
RequestId.inputvalidation=ALPHA_NUMERIC|-
SourceDeviceId.inputvalidation=ALPHA_NUMERIC
VehicleId.inputvalidation=ALPHA_NUMERIC
```

## Built With Dependencies

|                                                 Dependency                                                 | Purpose                                            |
|:----------------------------------------------------------------------------------------------------------:|:---------------------------------------------------|
|                           [Ignite Utils](https://github.com/HARMANInt/ics/utils)                           | Logging Support                                    |
|                      [Spring Framework](https://spring.io/projects/spring-framework)                       | For writing tests                                  |
|                                     [Maven](https://maven.apache.org/)                                     | Dependency Management                              |
|                                     [Junit](https://junit.org/junit5/)                                     | Testing framework                                  |
|                                    [Mockito](https://site.mockito.org/)                                    | Test Mocking framework                             |
|                            [Power Mock](https://github.com/powermock/powermock)                            | Test Mocking framework with extra mocking features |
| [Fast Serialization](https://github.com/RuedigerMoeller/fast-serialization/tree/master?tab=readme-ov-file) | FST serialization support                          |


## How to contribute

Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on our contribution guidelines, and the process for submitting pull requests to us.

## Code of Conduct

Please read [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) for details on our code of conduct.

## Authors

* **Kaushal Arora** - *Initial work* 
* **Ashish Kumar Singh** - *Coding guidelines*

See also the list of [contributors](https://github.com/HARMANInt/ics/transformers/contributors) who participated in this project.

## Security Contact Information

Please read [SECURITY.md](./SECURITY.md) to raise any security related issues.

## Support
Contact the project developers via the project's "dev" list - https://accounts.eclipse.org/mailing-list/ecsp-dev


## Troubleshooting

Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on how to raise an issue and submit a pull request to us.

## License

This project is licensed under the XXX License - see the [LICENSE.md](./LICENSE.md) file for details.

## Announcements

All updates to this library are documented in our [Release Notes](./release_notes.txt) and [releases](https://github.com/HARMANInt/ics/transformers/releases).
For the versions available, see the [tags on this repository](https://github.com/HARMANInt/ics/transformers/tags).

[sonar]:https://ignitestoretool.ahanet.net/dashboard?id=com.harman.ignite%3Aignite-transformers
[sonar img]:

[coverage]:https://ignitestoretool.ahanet.net/component_measures?id=com.harman.ignite%3Aignite-transformers&metric=coverage&view=list
[coverage img]: 

[license]: ./LICENSE.md
[license img]: https://img.shields.io/badge/license-GNU%20LGPL%20v2.1-blue.svg

[artifactory]: https://artifactory-fr.harman.com:443/artifactory/ignite-libs/com/harman/ignite/ignite-transformers/
[artifactory img]: https://artifactory-fr.harman.com/ui/

[status img]: https://jenkins-ignite.nh.ad.harman.com/buildStatus/icon?job=Ignite_Team%2FIgnite_Core%2FBuild_and_Deployment%2Ftransformers%2Fmaster
[status]: https://jenkins-ignite.nh.ad.harman.com/job/Ignite_Team/job/Ignite_Core/job/Build_and_Deployment/job/transformers/job/master/



