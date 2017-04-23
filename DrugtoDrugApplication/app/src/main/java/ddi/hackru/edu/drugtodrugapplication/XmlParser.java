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
        parser.setInput(in, null);
        parser.nextTag(); // this gets us the rxnormdata
        System.out.println(parser.getName());
        parser.nextTag(); // this gets us the idGroup
        System.out.println(parser.getName());
        parser.nextTag(); // this gets us the name
        System.out.println(parser.getText());
        String drugName = parser.nextText();
        System.out.println("drugname:" + drugName);

        parser.nextTag();
        System.out.println(parser.getName());
        String rxnormid = null;
        if(parser.getEventType() != XmlPullParser.END_TAG) {
            rxnormid = parser.nextText();
        }

        in.close();
        if(rxnormid == null)
            return null;
        else
            return new Medication(drugName, rxnormid);
    }

}
