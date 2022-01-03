# iCal4j Connector

A client library with support for various iCalendar servers, including support for CalDAV.

## Overview

This library provides client support for backend calendar services using the iCal4j object model. The
most popular services implement DAV extensions for Calendaring (CalDAV) and VCard (CardDAV).

## CalDAV and CardDAV

The iCal4j Connector now supports three approaches for integrating with CalDAV and CardDAV servers. First
there is a low-level DAV client that supports HTTP methods for communicating with WebDAV servers. Built on
this is the DAVResource implementation that supports response caching and path abstraction for different
server implementations. Finally the Store and Collection interfaces provide a high-level abstraction for
CalDAV and CardDAV resource management including collection discovery.

### DAV Client

The DAV client may be used to perform explicit CalDAV and CardDAV operations against a specified URL. A
DAV client instance is initialised with a server URL and configuration options.

```java
DavClientConfiguration configuration = new DavClientConfiguration().withPreemptiveAuth(true)
        .withFollowRedirects(true);
DavClientFactory clientFactory = new DavClientFactory(configuration);

DavClient client = clientFactory.newInstance("https://dav.example.com");
```    

Note that whilst the `DavClientFactory` currently returns a concrete type, it is recommended to use
interfaces for local references.

```java
CalDavSupport caldav = clientFactory.newInstance("https://dav.example.com/.wellknown/caldav");

CardDavSupport carddav = clientFactory.newInstance("https://dav.example.com/.wellknown/carddav");
```

#### Authentication

TBD.

#### Methods

New calendar collections are created via the `MKCALENDAR` method.

```java
DavPropertySet props = new DavPropertySet();
props.add(new DavPropertyBuilder<>().name(DavPropertyName.DISPLAYNAME).value('Test Collection').build());
props.add(new DavPropertyBuilder<>().name(CalDavPropertyName.CALENDAR_DESCRIPTION)
        .value('A simple mkcalendar test').build());
caldav.mkCalendar('/admin/test', props);
```

Existing calendar resources are retrieved via the `GET` method.

```java
Calendar calendar = caldav.getCalendar('/admin/test');
```

Resource and collection metadata is accessed via the `PROPFIND` method.

```java
DavPropertySet props = caldav.propFind('/admin', SecurityConstants.PRINCIPAL_COLLECTION_SET);
```

### DavResource

A `DavResource` is a higher-level abstraction of a resource path that supports hierarchical discovery of
resources and caching of properties.

```java
DavClientConfiguration configuration = new DavClientConfiguration().withPreemptiveAuth(true)
        .withFollowRedirects(true);
DavClientFactory clientFactory = new DavClientFactory(configuration);

DavLocatorFactory locatorFactory = new CalDavLocatorFactory(PathResolver.RADICALE);
DavResourceLocator locator = locatorFactory.createResourceLocator("https://dav.example.com",
        "user", "testcal");

DavResourceFactory resourceFactory = new CalDavResourceFactory(clientFactory);
DavResource resource = resourceFactory.createResource(locator, null);
```

A `DavResource` may be queried for common metadata properties.

```java
if (resource.isCollection()) {
        System.out.println("Collection name:" + resource.getDisplayName());
        System.out.println("Collection exists:" + resource.exists());
        System.out.println("Collection description:"
            + resource.getProperty(CalDavPropertyName.CALENDAR_DESCRIPTION).getValue());
}
```

A `DavResource` may be altered by adding and removing properties.

```java
// overwrite resource description property
resource.setProperty(new DavPropertyBuilder<>().name(CalDavPropertyName.CALENDAR_DESCRIPTION)
        .value('A simple mkcalendar test').build());

// unset calendar timezone
resource.removeProperty(CalDavPropertyName.CALENDAR_TIMEZONE);

// add and remove properties with a single request
resource.alterProperties(Arrays.asList(
        new DavPropertyBuilder<>().name(CalDavPropertyName.CALENDAR_DESCRIPTION)
            .value('A simple mkcalendar test').build(),
        CalDavPropertyName.CALENDAR_TIMEZONE
));
```

Child and sibling resources are also easily accessed.

```java
// add a child resource
resource.addMember(...);

// add a sibling resource
resource.getCollection().addMember(...);

// remove all children
for (DavResource child : resouce.getMembers()) {
        resource.removeMember(child);
}
```

## References

* [RFC3744](https://datatracker.ietf.org/doc/html/rfc3744) (WebDAV Access Control Protocol)
* [RFC4331](https://www.rfc-editor.org/rfc/rfc4331.html) (Quota and size properties for DAV)
* [RFC4791](https://www.rfc-editor.org/rfc/rfc4791.html) (CalDAV)
* [RFC4918](https://www.rfc-editor.org/rfc/rfc4918.html) (WebDAV)
* [RFC5397](https://www.rfc-editor.org/rfc/rfc5397.html) (WebDAV Current Principal Extension)
* [RFC5545](https://tools.ietf.org/html/rfc5545) (iCalendar)
* [RFC6350](https://datatracker.ietf.org/doc/html/rfc6350) (vCard)
* [RFC6352](https://www.rfc-editor.org/rfc/rfc6352.html) (CardDAV)
* [RFC7953](https://datatracker.ietf.org/doc/html/rfc7953) (iCalendar Availability)
