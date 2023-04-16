package fr.greencodeinitiative.html.rules;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarProduct;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.Version;

public class HtmlRulesDefinitionTest {

    @Test
    public void createExternalRepository() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        new HtmlRulesDefinition(new MockedSonarRuntime()).define(context);
        assertThat(context.repositories()).hasSize(1);

        RulesDefinition.Repository repository = context.repositories().get(0);
        assertThat(repository.isExternal()).isFalse();
        assertThat(repository.language()).isEqualTo("web");
        assertThat(repository.key()).isEqualTo("ecocode-html");
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
