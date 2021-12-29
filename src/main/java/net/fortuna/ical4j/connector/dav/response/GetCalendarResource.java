package net.fortuna.ical4j.connector.dav.response;

import net.fortuna.ical4j.connector.MediaType;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class GetCalendarResource extends AbstractResponseHandler<Calendar> {

    @Override
    public Calendar handleResponse(HttpResponse response) throws IOException {
        InputStream content = getContent(response, MediaType.ICALENDAR_2_0);
        if (content != null) {
            try {
                CalendarBuilder builder = new CalendarBuilder();
                return builder.build(content);
            } catch (ParserException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
