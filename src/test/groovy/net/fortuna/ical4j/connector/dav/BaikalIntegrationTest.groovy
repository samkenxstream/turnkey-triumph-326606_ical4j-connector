package net.fortuna.ical4j.connector.dav

import net.fortuna.ical4j.connector.AbstractIntegrationTest

import static net.fortuna.ical4j.connector.dav.SupportedFeature.*

class BaikalIntegrationTest extends AbstractIntegrationTest implements BaikalTestSupport {

    def 'test client authentication'() {
        given: 'a dav client instance'
        URL href = URI.create(getContainerUrl()).toURL()
        def client = new DavClientFactory().withPreemptiveAuth(true)
                .newInstance(href)

        when: 'a session is started'
        def supportedFeatures = client.begin(getCredentialsProvider())

        then: 'authentication is successful'
        client.getSupportedFeatures() == Arrays.asList(CALENDAR_ACCESS, ADDRESSBOOK, EXTENDED_MKCOL)
    }
}
