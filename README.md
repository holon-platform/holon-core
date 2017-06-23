# Holon platform Core module

This is the __core__ module of the [Holon Platform](https://holon-platform.com) and represents the platform foundation, providing the definition of the overall architecture, base structures and APIs.

## Getting started

### System requirements

The Holon Platform is built using __Java 8__, so you need a JRE/JDK version 8 or above to use the platform artifacts.

### Releases

See [releases](https://github.com/holon-platform/holon-core/releases) for the available releases. Each release tag provides a link to the closed issues.

### Obtain the artifacts

The [Holon Platform](https://holon-platform.com) is open source and licensed under the [Apache 2.0 license](LICENSE.md). All the artifacts (including binaries, sources and javadocs) are available from the [Maven Central](https://mvnrepository.com/repos/central) repository.

The Maven __group id__ for this module is `com.holon-platform.core` and a _BOM (Bill of Materials)_ is provided to obtain the module artifacts:

_Maven BOM:_
```xml
<dependencyManagement>
    <dependency>
        <groupId>com.holon-platform.core</groupId>
        <artifactId>holon-bom</artifactId>
        <version>5.0.0</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

See the module [documentation](https://holon-platform.com/docs/current/reference/holon-core.html) for further information about each artifact provided by this module.

### Using the Platform BOM

The [Holon Platform](https://holon-platform.com) provides an overall Maven _BOM (Bill of Materials)_ to easily obtain all the available platform artifacts:

_Platform Maven BOM:_
```xml
<dependencyManagement>
    <dependency>
        <groupId>com.holon-platform</groupId>
        <artifactId>bom</artifactId>
        <version>${platform-version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

See the platform [documentation](https://holon-platform.com/docs/current/reference) for detailed information about platform architecture and projects setup.

## Getting help

* Check the [platform documentation](https://holon-platform.com/docs/current/reference) or the specific [module documentation](https://holon-platform.com/docs/current/reference/holon-core.html).

* Ask a question on [Stack Overflow](http://stackoverflow.com). We monitor the [`holon-platform`](http://stackoverflow.com/tags/holon-platform) tag.

* Report an [issue](https://github.com/holon-platform/holon-core/issues).

* A [commercial support](https://holon-platform.com/services) is available too.

## Build from sources



## Contribute

See [Contributing to the Holon Platform](https://github.com/holon-platform/platform/blob/master/CONTRIBUTING.md).

## License

All the [Holon Platform](https://holon-platform.com) modules are _Open Source_ software released under the [Apache 2.0 license](LICENSE.md).
