package fr.greencodeinitiative.html.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.node.TagNode;

@Rule(key = "AvoidAutoplayCheck")
public class AvoidAutoplayCheck extends AbstractPageCheck {

    @Override
    public void startElement(TagNode node) {
        if (isAudioTag(node) && hasAutoplayAttribute(node)) {
            createViolation(node, "Remove autoplay attribute to this audio");
        } else if (isVideoTag(node) && hasAutoplayAttribute(node)) {
            createViolation(node, "Remove autoplay attribute to this video");
        }
    }
    
    private static boolean isAudioTag(TagNode node) {
        return "AUDIO".equalsIgnoreCase(node.getNodeName());
    }
    
    private static boolean isVideoTag(TagNode node) {
        return "VIDEO".equalsIgnoreCase(node.getNodeName());
    }
    
    private static boolean hasAutoplayAttribute(TagNode node) {
        return node.hasProperty("AUTOPLAY");
    }
}
