package fr.greencodeinitiative.html;

import org.sonar.api.Plugin;

import fr.greencodeinitiative.html.core.HtmlSensor;
import fr.greencodeinitiative.html.rules.HtmlRulesDefinition;

public class HtmlPlugin implements Plugin {

    @Override
    public void define(Context context) {
      context.addExtensions(
        // web rules repository
        HtmlRulesDefinition.class,
        // custom HTML sensor
        HtmlSensor.class
      );
    }

}
