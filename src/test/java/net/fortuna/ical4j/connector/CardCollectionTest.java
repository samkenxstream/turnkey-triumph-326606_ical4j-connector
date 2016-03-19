/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

import net.fortuna.ical4j.vcard.VCardBuilder;
import net.fortuna.ical4j.vcard.VCardFileFilter;
import org.junit.Ignore;

/**
 * 
 *
 * @author Ben
 *
 * Created on: 24/02/2009
 *
 * $Id$
 */
@Ignore
public class CardCollectionTest<T extends CardCollection> extends
        ObjectCollectionTest<T> {

    private static final String[] SUPPORTED_COMPONENTS = {};
    
    /**
     * @param testMethod
     * @param lifecycle
     * @param username
     * @param password
     * @param supportedComponents
     */
    public CardCollectionTest(String testMethod, ObjectStoreLifecycle<T> lifecycle, String username,
            char[] password) {
        super(testMethod, lifecycle, username, password, SUPPORTED_COMPONENTS);
    }

    /* (non-Javadoc)
     * @see net.fortuna.ical4j.connector.ObjectCollectionTest#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        File[] samples = new File("etc/samples/cards/").listFiles((FilenameFilter) VCardFileFilter.INSTANCE);
        for (File sample : samples) {
            VCardBuilder builder = new VCardBuilder(new FileInputStream(sample));
            getCollection().addCard(builder.build());
        }
    }
}
