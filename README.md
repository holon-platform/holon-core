# Holon platform Core module

> Latest release: [5.7.0](#obtain-the-artifacts)

This is the __core__ module of the [Holon Platform](https://holon-platform.com) and represents the platform foundation, providing the definition of the overall architecture, base structures and APIs.

The module highlights are:

* `Context` resources
* Configuration APIs
* Data _validation_ APIs
* Localization and internationalization support
* The `Property` model and the `Datastore` API
* Java Beans support
* RESTful Java client
* Authentication and authorization
* [JSON Web Tokens](https://jwt.io) (`JWT`) support
* [Spring](https://spring.io), [Spring Security](https://projects.spring.io/spring-security) and [Spring Boot](https://projects.spring.io/spring-boot/) integration
* Spring _tenant_ scope 

See the module [documentation](https://docs.holon-platform.com/current/reference/holon-core.html) for details.

Just like any other platform module, this artifact is part of the [Holon Platform](https://holon-platform.com) ecosystem, but can be also used as a _stand-alone_ library.

See [Getting started](#getting-started) and the [platform documentation](https://docs.holon-platform.com/current/reference) for further details.

## At-a-glance overview

_Property model definition:_
```java
public interface Subject {

	static NumericProperty<Long> ID = NumericProperty.longType("id");
	static StringProperty NAME = StringProperty.create("name");
	static StringProperty SURNAME = StringProperty.create("surname");
	static TemporalProperty<LocalDate> BIRTH = TemporalProperty.localDate("birth");
	static BooleanProperty ACTIVE = BooleanProperty.create("active");
	static VirtualProperty<String> FULL_NAME = VirtualProperty.create(String.class,
			propertyBox -> propertyBox.getValue(NAME) + " " + propertyBox.getValue(SURNAME));
	
	static PropertySet<?> SUBJECT = PropertySet.of(ID, NAME, SURNAME, BIRTH, ACTIVE, FULL_NAME);

}
```

_Property configuration:_
```java
static StringProperty NAME = StringProperty.create("name").message("Name").messageCode("localization.name")
			.withConfiguration("my-config", "my-value");
```

_Property value converter:_
```java
static StringProperty INTEGER_MODEL = StringProperty.create("integer_value").converter(Integer.class,
			integer -> String.valueOf(integer), string -> Integer.valueOf(string));
```

_Property validators:_
```java
static StringProperty NAME = StringProperty.create("name")
			.withValidator(Validator.notBlank()).withValidator(Validator.max(50));
```

_Property presenters and renderers:_
```java
String value = NAME.present("A value");
MyType myType = NAME.render(MyType.class);
```

_PropertyBox:_
```java
PropertyBox propertyBox = PropertyBox.create(SUBJECT);
		
String name = propertyBox.getValue(NAME);
Optional<String> oname = propertyBox.getValueIfPresent(NAME);
propertyBox.setValue(NAME, "John");
propertyBox.propertyValues().forEach(propertyValue -> {
	Property<?> property = propertyValue.getProperty();
	Object value = propertyValue.getValue();
});
```

_Datastore:_
```java
DataTarget<?> TARGET = DataTarget.named("subjects");
Datastore datastore = getDatastore();

Stream<PropertyBox> results = datastore.query().target(TARGET)
	.filter(NAME.contains("a").and(SURNAME.isNotNull())).sort(BIRTH.desc()).stream(SUBJECT);

Stream<String> names = datastore.query(TARGET).aggregate(SURNAME).stream(NAME.max());

Optional<String> name = datastore.query(TARGET).filter(ID.eq(1L)).findOne(NAME);

datastore.insert(TARGET, PropertyBox.builder(SUBJECT).set(ID, 1L).set(NAME, "John").set(ACTIVE, true).build());
datastore.bulkUpdate(TARGET).set(ACTIVE, true).filter(BIRTH.lt(LocalDate.now())).execute();

datastore.query(TARGET).filter(ID.eq(1L)).findOne(SUBJECT).ifPresent(subject -> datastore.delete(TARGET, subject));
```

_Bean PropertySet and Datastore:_
```java
class MyBean {
	private @NotNull Long id;
	private @Caption("The name") String name;
	private @Caption("The surname") String surname;
	/* getters and setters omitted */
}

BeanPropertySet<MyBean> propertySet = BeanPropertySet.create(MyBean.class);
		
PathProperty<?> name = propertySet.property("name");
PathProperty<String> typedName = propertySet.property("name", String.class);
		
BeanDatastore datastore = BeanDatastore.of(getDatastore());
		
Stream<MyBean> results = datastore.query(MyBean.class).filter(propertySet.property("name").eq("John")).stream();
		
datastore.save(new MyBean());
```

_Realm:_
```java
Realm realm = Realm.builder().withAuthenticator(Authenticator.create(MyAuthenticationToken.class, token -> {
	if ("test".equals(token.getPrincipal())) {
		return Authentication.builder("test").withPermission("ROLE1").build();
	}
	throw new UnknownAccountException();
}))
.withDefaultAuthorizer().build();

Realm.builder().withAuthenticator(Account.authenticator(id -> Optional.of(Account.builder(id).build()))).build();
```

_AuthContext:_
```java
AuthContext context = AuthContext.create(realm);
context.authenticate(AuthenticationToken.accountCredentials("test", "pwd"));
		
Optional<Authentication> authentication = context.getAuthentication();
boolean permitted = context.isPermitted("ROLE1", "ROLE2");
```

_RestClient:_
```java
RestClient client = RestClient.forTarget("https://rest.api.example");
		
ResponseEntity<TestData> response = client.request()
	.path("test/{id}").resolve("id", 123)
	.accept(MediaType.APPLICATION_JSON)
	.header("MY_HEADER", "my-value")
	.authorizationBearer("An389fz56xsr7")
	.get(TestData.class);
HttpStatus status = response.getStatus();
Optional<TestData> payload = response.getPayload();
		
Optional<TestData> data = client.request().path("test/{id}").resolve("id", 123)
				.getForEntity(TestData.class);
		
List<TestData> results = client.request().path("test").getAsList(TestData.class);
		
client.request().path("test").post(RequestEntity.json(new TestData()));
		
Optional<PropertyBox> propertyBox = client.request().path("test2")
				.propertySet(PROPERTIES).getForEntity(PropertyBox.class); 
```

_LocalizationContext:_
```java
LocalizationContext localizationContext = LocalizationContext.builder()
	.withMessageProvider(MessageProvider.fromProperties("messages").build())
	.withDefaultDateTemporalFormat(TemporalFormat.MEDIUM)
	.withInitialLocale(Locale.US)
	.build();
		
localizationContext.localize(Locale.ITALY);
		
String localized = localizationContext.getMessage("message.code", "Default message");
		
String formatted = localizationContext.format(LocalDate.now());
formatted = localizationContext.format(123.4d);
		
Optional<LocalizationContext> current = LocalizationContext.getCurrent();
```

See the [module documentation](https://docs.holon-platform.com/current/reference/holon-core.html) for the user guide and a full set of examples.

## Code structure

See [Holon Platform code structure and conventions](https://github.com/holon-platform/platform/blob/master/CODING.md) to learn about the _"real Java API"_ philosophy with which the project codebase is developed and organized.

## Getting started

### System requirements

The Holon Platform is built using __Java 11__, so you need a JRE/JDK version 11 or above to use the platform artifacts.

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
        <version>5.7.0</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

See the [Artifacts list](#artifacts-list) for a list of the available artifacts of this module.

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

See the [Artifacts list](#artifacts-list) for a list of the available artifacts of this module.

### Build from sources

You can build the sources using Maven (version 3.3.x or above is recommended) like this: 

`mvn clean install`

## Getting help

* Check the [platform documentation](https://docs.holon-platform.com/current/reference) or the specific [module documentation](https://docs.holon-platform.com/current/reference/holon-core.html).

* Ask a question on [Stack Overflow](http://stackoverflow.com). We monitor the [`holon-platform`](http://stackoverflow.com/tags/holon-platform) tag.

* Report an [issue](https://github.com/holon-platform/holon-core/issues).

* A [commercial support](https://holon-platform.com/services) is available too.

## Examples

See the [Holon Platform examples](https://github.com/holon-platform/holon-examples) repository for a set of example projects.

## Contribute

See [Contributing to the Holon Platform](https://github.com/holon-platform/platform/blob/master/CONTRIBUTING.md).

[![Gitter chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/holon-platform/contribute?utm_source=share-link&utm_medium=link&utm_campaign=share-link) 
Join the __contribute__ Gitter room for any question and to contact us.

## License

All the [Holon Platform](https://holon-platform.com) modules are _Open Source_ software released under the [Apache 2.0 license](LICENSE).

## Artifacts list

Maven _group id_: `com.holon-platform.core`

Artifact id | Description
----------- | -----------
`holon-core` | Platform core components, services and APIs
`holon-http` | HTTP messages support
`holon-async-http` | Asynchronous HTTP messages support
`holon-async-datastore` | Asynchronous Datastore API
`holon-auth` | Authentication and Authorization
`holon-auth-jwt` | JSON Web Tokens support 
`holon-spring` | Spring integration
`holon-spring-security` | Spring Security integration
`holon-spring-boot` | Spring Boot integration
`holon-starter` | Base Spring Boot starter
`holon-starter-security` | Base Spring Boot starter with Spring Security integration
`holon-starter-test` | Base Spring Boot starter for Unit tests
`holon-bom` | Bill Of Materials
`holon-bom-platform` | Bill Of Materials including external dependencies
`documentation-core` | Documentation
