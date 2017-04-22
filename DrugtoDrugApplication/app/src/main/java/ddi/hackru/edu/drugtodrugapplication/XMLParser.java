package ddi.hackru.edu.drugtodrugapplication;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jasper Bae on 4/22/2017.
 *
 * Based on https://developer.android.com/training/basics/network-ops/xml.html
 *
 */
public class XmlParser
{

    private static final String ns = null;

    public Medication parseMedication(InputStream in) throws XmlPullParserException, IOException
    {
        XmlPullParser parser = Xml.newPullParser();
        parser.require(XmlPullParser.START_TAG, ns, "rxnormdata");
        parser.setInput(in, null);
        parser.nextTag(); // this gets us the rxnormdata
        parser.nextTag(); // this gets us the idGroup
        parser.nextTag(); // this gets us the name
        String drugName = parser.getName();
        parser.nextTag();
        String rxnormid = null;
        if(parser.getEventType() != XmlPullParser.END_TAG)
            rxnormid = parser.getText();
        in.close();
        if(rxnormid == null)
            return null;
        else
            return new Medication();
    }

}
