package fr.greencodeinitiative.html;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarProduct;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;

public class HtmlPluginTest {

    @Test
    public void extensions() {
        Plugin.Context context = new Plugin.Context(new MockedSonarRuntime());
        new HtmlPlugin().define(context);
        assertThat(context.getExtensions()).hasSize(2);
    }

    private static class MockedSonarRuntime implements SonarRuntime {

        @Override
        public Version getApiVersion() {
            return Version.create(9, 9);
        }

        @Override
        public SonarProduct getProduct() {
            return SonarProduct.SONARQUBE;
        }

        @Override
        public SonarQubeSide getSonarQubeSide() {
            return SonarQubeSide.SCANNER;
        }

        @Override
        public SonarEdition getEdition() {
            return SonarEdition.COMMUNITY;
        }
    }

}
